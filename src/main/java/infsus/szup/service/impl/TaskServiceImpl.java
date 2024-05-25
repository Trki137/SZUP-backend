package infsus.szup.service.impl;

import infsus.szup.mapper.TaskMapper;
import infsus.szup.model.dto.task.*;
import infsus.szup.model.entity.*;
import infsus.szup.model.enums.Status;
import infsus.szup.repository.*;
import infsus.szup.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final ProjectDao projectDao;
    private final TaskDao taskDao;
    private final TeamDao teamDao;
    private final UserDao userDao;
    private final StatusDao statusDao;
    private final PriorityDao priorityDao;
    private final TaskMapper taskMapper;

    @Transactional
    @Override
    public TaskResponseDTO createTask(Long projectId, Long teamId, CreateTaskRequestDTO createTaskRequestDTO) {
        final ProjectEntity project = projectDao.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Project with id %d doesn't exists", projectId))
        );

        final TeamEntity team = teamDao.findById(teamId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Team with id %d not found!", teamId))
        );

        boolean isTeamInProject = project.getTeams().stream().anyMatch(projectTeam -> projectTeam.equals(team));
        if (!isTeamInProject) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("Team %s doesn't belong to this project", team.getTeamName()));
        }

        final UserEntity taskOwner = userDao.findById(createTaskRequestDTO.taskOwnerId()).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id %d not found!", createTaskRequestDTO.taskOwnerId()))
        );

        final UserEntity taskSolver = userDao.findById(createTaskRequestDTO.taskSolverId()).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id %d not found!", createTaskRequestDTO.taskSolverId()))
        );

        boolean isPartOfTeam = team.getTeamMembers().stream().anyMatch(member -> member.getTeamMember().equals(taskSolver));
        if (!isPartOfTeam) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("User %s doesn't belong to team %s", taskSolver.getFirstName() + " " + taskSolver.getLastName(), team.getTeamName()));
        }

        TaskEntity task = taskMapper.toTaskEntity(createTaskRequestDTO);
        task.setProject(project);
        task.setTaskOwner(taskOwner);
        task.setTaskSolver(taskSolver);
        task.setTaskSetDate(LocalDateTime.now());
        task.setCurrentStatus(statusDao.getReferenceById(Status.NOT_STARTED.getId()));
        task.setPriority(priorityDao.getReferenceById(createTaskRequestDTO.taskPriorityId()));
        task = taskDao.save(task);

        return taskMapper.toTaskResponseDTO(task);
    }

    @Transactional(readOnly = true)
    @Override
    public TaskDetailsDTO getTaskDetails(Long taskId) {
        return taskMapper.toTaskDetails(taskDao.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Task with id = %d not found.", taskId))
        ));
    }

    @Transactional
    @Override
    public void deleteTask(Long taskId) {
        final TaskEntity task = taskDao.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Task with id = %d not found.", taskId))
        );

        taskDao.delete(task);
    }

    @Transactional
    @Override
    public TaskDetailsDTO updateStatus(UpdateTaskStatusDTO updateTaskStatusDTO) {
        final Long taskId = updateTaskStatusDTO.taskId();
        final Long updatedByUserId = updateTaskStatusDTO.updatedByUserId();
        final Integer nextStatusId = updateTaskStatusDTO.newTaskStatusId();

        final TaskEntity task = taskDao.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Task with id %d not found.", taskId))
        );

        final StatusEntity nextStatus = statusDao.findById(nextStatusId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Status with id %d doesn't exists", nextStatusId))
        );

        final UserEntity updateByUser = userDao.findById(updatedByUserId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id %d not found!", updatedByUserId))
        );

        final StatusEntity currentStatus = task.getCurrentStatus();
        if (updateByUser.equals(task.getTaskSolver())) {
            if (Status.NOT_STARTED.getId() == currentStatus.getId()) {
                if (Status.IN_PROGRESS.getId() != nextStatusId) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Neispravan novi status");
                }
            }

            if (Status.IN_PROGRESS.getId() == currentStatus.getId()) {
                if (Status.IN_REVIEW.getId() != nextStatusId) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Neispravan novi status");
                }
            }
        }

        if (updateByUser.equals(task.getTaskOwner())) {
            if (Status.IN_REVIEW.getId() == currentStatus.getId()) {
                if (Status.CLOSED.getId() != nextStatusId && Status.IN_PROGRESS.getId() == nextStatusId) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Neispravan novi status");
                }
            }
        }

        task.setCurrentStatus(nextStatus);
        return taskMapper.toTaskDetails(task);
    }

    @Transactional
    @Override
    public TaskDetailsDTO updateTask(Long taskId, UpdateTaskDTO updateTaskDTO) {
        final TaskEntity task = taskDao.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Task with id %d not found.", taskId))
        );


        taskMapper.updateTask(task, updateTaskDTO);
        task.setPriority(priorityDao.getReferenceById(updateTaskDTO.priorityId()));
        return taskMapper.toTaskDetails(task);
    }

    @Transactional
    @Override
    public TaskDetailsDTO changeTaskSolver(Long projectId, Long taskId, TaskSolverChangeDTO taskSolverChangeDTO) {
        final ProjectEntity project = projectDao.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Project with id %d doesn't exists", projectId))
        );

        final TeamEntity team = teamDao.findById(taskSolverChangeDTO.newTeamId()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Team with id %d not found!", taskSolverChangeDTO.newTeamId()))
        );

        boolean isTeamInProject = project.getTeams().stream().anyMatch(projectTeam -> projectTeam.equals(team));
        if (!isTeamInProject) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("Team %s doesn't belong to this project", team.getTeamName()));
        }

        final UserEntity taskSolver = userDao.findById(taskSolverChangeDTO.newTaskSolverId()).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id %d not found!", taskSolverChangeDTO.newTaskSolverId()))
        );

        final UserEntity changedByUser = userDao.findById(taskSolverChangeDTO.changedById()).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id %d not found!", taskSolverChangeDTO.changedById()))
        );

        boolean isPartOfTeam = team.getTeamMembers().stream().anyMatch(member -> member.getTeamMember().equals(taskSolver));
        if (!isPartOfTeam) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("User %s doesn't belong to team %s", taskSolver.getFirstName() + " " + taskSolver.getLastName(), team.getTeamName()));
        }

        isPartOfTeam = team.getTeamMembers().stream().anyMatch(member -> member.getTeamMember().equals(changedByUser));
        if (!isPartOfTeam) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("User %s doesn't belong to team %s", changedByUser.getFirstName() + " " + changedByUser.getLastName(), team.getTeamName()));
        }

        final TaskEntity task = taskDao.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Task with id %d not found.", taskId))
        );

        task.setTaskSolver(taskSolver);
        return taskMapper.toTaskDetails(task);
    }

    @Transactional(readOnly = true)
    @Override
    public UserTasksDTO getUserTasks(Long projectId, Long userId) {
        final UserEntity user = userDao.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id %d not found!", userId))
        );

        final ProjectEntity project = projectDao.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Project with id %d doesn't exists", projectId))
        );


        return new UserTasksDTO(
                taskDao.findByTaskSolverAndProject(user, project).stream().map(taskMapper::toTaskResponseDTO).toList(),
                taskDao.findByTaskOwnerAndProject(user, project).stream().map(taskMapper::toTaskResponseDTO).toList()
        );
    }
}
