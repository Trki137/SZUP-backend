package infsus.szup.service.integration;


import infsus.szup.model.dto.task.*;
import infsus.szup.model.entity.TaskEntity;
import infsus.szup.repository.TaskDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskApiIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TaskDao taskDao;

    private static HttpHeaders headers;

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    private String createBaseURL() {
        return String.format("http://localhost:%d/api", port);
    }


    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    public void testCreateTask_Success() {
        final Long projectId = 1L;
        final Long teamId = 1L;
        final Long taskOwnerId = 3L;
        final Long taskSolverId = 4L;
        final Integer taskPriorityId = 1;


        final String createTaskURL = String.format("%s/project/%d/team/%d/task/create-task", createBaseURL(), projectId, teamId);
        final CreateTaskRequestDTO createTaskRequestDTO = new CreateTaskRequestDTO("Test task", null, "Test description", null, taskOwnerId, taskSolverId, taskPriorityId);
        HttpEntity<CreateTaskRequestDTO> entity = new HttpEntity<>(createTaskRequestDTO, headers);
        ResponseEntity<TaskResponseDTO> response = testRestTemplate.exchange(
                createTaskURL, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
                }
        );
        final TaskResponseDTO responseDTO = response.getBody();
        assert responseDTO != null;
        assertEquals(response.getStatusCode().value(), 200);
        assertEquals(responseDTO.taskName(), createTaskRequestDTO.taskName());

        TaskEntity createdTask = taskDao.findById(responseDTO.id()).orElse(null);
        assert createdTask != null;

        assertEquals(createTaskRequestDTO.taskName(), createdTask.getTaskName());
        assertEquals(createTaskRequestDTO.description(), createdTask.getDescription());
        assertEquals(createTaskRequestDTO.taskOwnerId(), createdTask.getTaskOwner().getId());
        assertEquals(createTaskRequestDTO.taskSolverId(), createdTask.getTaskSolver().getId());
        assertEquals(createTaskRequestDTO.taskPriorityId(), createdTask.getPriority().getId());

        assertEquals(projectId, createdTask.getProject().getId());
    }

    @Test
    public void testCreateTask_TeamNotInProject_ShouldFail() {
        final Long projectId = 1L;
        final Long teamId = 3L;
        final Long taskOwnerId = 3L;
        final Long taskSolverId = 4L;
        final Integer taskPriorityId = 1;


        final String createTaskURL = String.format("%s/project/%d/team/%d/task/create-task", createBaseURL(), projectId, teamId);
        final CreateTaskRequestDTO createTaskRequestDTO = new CreateTaskRequestDTO("Test task", null, "Test description", null, taskOwnerId, taskSolverId, taskPriorityId);
        HttpEntity<CreateTaskRequestDTO> entity = new HttpEntity<>(createTaskRequestDTO, headers);
        ResponseEntity<TaskResponseDTO> response = testRestTemplate.exchange(
                createTaskURL, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testCreateTask_SolverNotInCorrectTeam_ShouldFail() {
        final Long projectId = 1L;
        final Long teamId = 1L;
        final Long taskOwnerId = 1L;
        final Long taskSolverId = 3L;
        final Integer taskPriorityId = 1;


        final String createTaskURL = String.format("%s/project/%d/team/%d/task/create-task", createBaseURL(), projectId, teamId);
        final CreateTaskRequestDTO createTaskRequestDTO = new CreateTaskRequestDTO("Test task", null, "Test description", null, taskOwnerId, taskSolverId, taskPriorityId);
        final HttpEntity<CreateTaskRequestDTO> entity = new HttpEntity<>(createTaskRequestDTO, headers);
        final ResponseEntity<TaskResponseDTO> response = testRestTemplate.exchange(
                createTaskURL, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void getTaskDetails_Success() {
        final Long taskId = 1L;

        final String createTaskURL = String.format("%s/task/%d", createBaseURL(), taskId);

        final HttpEntity<String> entity = new HttpEntity<>(null, headers);
        final ResponseEntity<TaskDetailsDTO> response = testRestTemplate.exchange(
                createTaskURL, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
                }
        );

        final TaskDetailsDTO taskDetailsDTO = response.getBody();
        assert taskDetailsDTO != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());

        final String expectedTaskName = "Task 1";
        final String expectedDescription = "Description 1";
        final Long expectedTaskOwner = 1L;
        final Long expectedTaskSolver = 4L;
        final Integer expectedStatusId = 1;
        final String expectedStatus = "Nije započeto";

        assertEquals(taskId, taskDetailsDTO.id());
        assertEquals(expectedTaskName, taskDetailsDTO.taskName());
        assertEquals(expectedDescription, taskDetailsDTO.description());
        assertNull(taskDetailsDTO.taskEndDate());
        assertNotNull(taskDetailsDTO.taskSetDate());
        assertEquals(expectedTaskOwner, taskDetailsDTO.taskOwner().id());
        assertEquals(expectedTaskSolver, taskDetailsDTO.taskSolver().id());
        assertEquals(expectedStatusId, taskDetailsDTO.currentStatus().id());
        assertEquals(expectedStatus, taskDetailsDTO.currentStatus().statusName());
    }


    @Test
    public void getTaskDetails_TaskNotFound_ShouldFail() {
        final Long taskId = 999L;
        final String getTaskDetailsURL = String.format("%s/task/%d", createBaseURL(), taskId);

        final HttpEntity<String> entity = new HttpEntity<>(null, headers);
        final ResponseEntity<String> response = testRestTemplate.exchange(
                getTaskDetailsURL, HttpMethod.GET, entity, new ParameterizedTypeReference<String>() {
                }
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deleteTask_Success() {
        final Long taskId = 2L;
        final String deleteTaskURL = String.format("%s/task/%d", createBaseURL(), taskId);

        final HttpEntity<String> entity = new HttpEntity<>(null, headers);
        final ResponseEntity<Void> response = testRestTemplate.exchange(
                deleteTaskURL, HttpMethod.DELETE, entity, new ParameterizedTypeReference<Void>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Optional<TaskEntity> task = taskDao.findById(taskId);
        assert task.isEmpty();
    }

    @Test
    public void deleteTask_TaskNotFound_ShouldFail() {
        final Long taskId = 999L;
        final String deleteTaskURL = String.format("%s/task/%d", createBaseURL(), taskId);

        final HttpEntity<String> entity = new HttpEntity<>(null, headers);
        final ResponseEntity<String> response = testRestTemplate.exchange(
                deleteTaskURL, HttpMethod.DELETE, entity, new ParameterizedTypeReference<String>() {
                }
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void updateTask_Success() {
        final Long taskId = 1L;
        final String updateTaskURL = String.format("%s/task/%d", createBaseURL(), taskId);

        final UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO(
                "Updated task name",
                LocalDateTime.now(),
                "Updated description",
                3
        );

        HttpEntity<UpdateTaskDTO> entity = new HttpEntity<>(updateTaskDTO, headers);
        final ResponseEntity<TaskDetailsDTO> response = testRestTemplate.exchange(
                updateTaskURL, HttpMethod.PUT, entity, new ParameterizedTypeReference<>() {
                }
        );

        final TaskDetailsDTO taskDetailsDTO = response.getBody();
        assert taskDetailsDTO != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());

        final Long expectedTaskOwner = 1L;
        final Long expectedTaskSolver = 4L;
        final Integer expectedStatusId = 1;
        final String expectedStatus = "Nije započeto";

        assertEquals(taskId, taskDetailsDTO.id());
        assertEquals(updateTaskDTO.taskName(), taskDetailsDTO.taskName());
        assertEquals(updateTaskDTO.description(), taskDetailsDTO.description());
        assertNotNull(taskDetailsDTO.taskEndDate());
        assertEquals(updateTaskDTO.taskEndDate(), taskDetailsDTO.taskEndDate());
        assertNotNull(taskDetailsDTO.taskSetDate());
        assertEquals(expectedTaskOwner, taskDetailsDTO.taskOwner().id());
        assertEquals(expectedTaskSolver, taskDetailsDTO.taskSolver().id());
        assertEquals(expectedStatusId, taskDetailsDTO.currentStatus().id());
        assertEquals(expectedStatus, taskDetailsDTO.currentStatus().statusName());
        assertEquals(updateTaskDTO.priorityId(), taskDetailsDTO.priority().id());

    }

    @Test
    public void updateTask_TaskNotFound_ShouldFail() {
        final Long taskId = 999L;
        final String updateTaskURL = String.format("%s/task/%d", createBaseURL(), taskId);


        final UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO(
                "Updated task name",
                LocalDateTime.now(),
                "Updated description",
                3
        );

        HttpEntity<UpdateTaskDTO> entity = new HttpEntity<>(updateTaskDTO, headers);
        final ResponseEntity<String> response = testRestTemplate.exchange(
                updateTaskURL, HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void changeTaskSolver_Success() {
        final Long projectId = 1L;
        final Long taskId = 1L;
        final String changeTaskSolverURL = String.format("%s/project/%d/task/%d", createBaseURL(), projectId, taskId);

        final Long newTeamId = 1L;
        final Long newTaskSolverId = 4L;
        final Long changedById = 4L;

        final TaskSolverChangeDTO taskSolverChangeDTO = new TaskSolverChangeDTO(newTeamId, newTaskSolverId, changedById);

        HttpEntity<TaskSolverChangeDTO> entity = new HttpEntity<>(taskSolverChangeDTO, headers);
        final ResponseEntity<TaskDetailsDTO> response = testRestTemplate.exchange(
                changeTaskSolverURL, HttpMethod.PUT, entity, new ParameterizedTypeReference<TaskDetailsDTO>() {
                }
        );

        final TaskDetailsDTO taskDetailsDTO = response.getBody();
        assert taskDetailsDTO != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());

        final String expectedTaskName = "Task 1";
        final String expectedDescription = "Description 1";
        final Long expectedTaskOwner = 1L;
        final Integer expectedStatusId = 1;
        final String expectedStatus = "Nije započeto";

        assertEquals(taskId, taskDetailsDTO.id());
        assertEquals(expectedTaskName, taskDetailsDTO.taskName());
        assertEquals(expectedDescription, taskDetailsDTO.description());
        assertNull(taskDetailsDTO.taskEndDate());
        assertNotNull(taskDetailsDTO.taskSetDate());
        assertEquals(expectedTaskOwner, taskDetailsDTO.taskOwner().id());
        assertEquals(newTaskSolverId, taskDetailsDTO.taskSolver().id());
        assertEquals(expectedStatusId, taskDetailsDTO.currentStatus().id());
        assertEquals(expectedStatus, taskDetailsDTO.currentStatus().statusName());
    }

    @Test
    public void changeTaskSolver_TaskNotFound_ShouldFail() {
        final Long projectId = 1L;
        final Long taskId = 999L;
        final String changeTaskSolverURL = String.format("%s/project/%d/task/%d", createBaseURL(), projectId, taskId);

        final Long newTeamId = 1L;
        final Long newTaskSolverId = 5L;
        final Long changedById = 1L;

        final TaskSolverChangeDTO taskSolverChangeDTO = new TaskSolverChangeDTO(newTeamId, newTaskSolverId, changedById);

        HttpEntity<TaskSolverChangeDTO> entity = new HttpEntity<>(taskSolverChangeDTO, headers);
        final ResponseEntity<String> response = testRestTemplate.exchange(
                changeTaskSolverURL, HttpMethod.PUT, entity, new ParameterizedTypeReference<String>() {
                }
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    public void getUserTasks_UserNotFound_ShouldFail() {
        final Long userId = 999L;
        final String getUserTasksURL = String.format("%s/task/all-tasks/user/%d", createBaseURL(), userId);

        final HttpEntity<String> entity = new HttpEntity<>(null, headers);
        final ResponseEntity<String> response = testRestTemplate.exchange(
                getUserTasksURL, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


}
