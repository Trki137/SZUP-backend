package infsus.szup.service.impl;

import infsus.szup.mapper.TaskMapper;
import infsus.szup.model.dto.task.CreateTaskRequestDTO;
import infsus.szup.model.dto.task.CreateTaskResponseDTO;
import infsus.szup.model.entity.ProjectEntity;
import infsus.szup.model.entity.TaskEntity;
import infsus.szup.model.entity.TeamEntity;
import infsus.szup.model.entity.UserEntity;
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
    public CreateTaskResponseDTO createTask(Long projectId, Long teamId, CreateTaskRequestDTO createTaskRequestDTO) {
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
        task.setTaskOwner(taskOwner);
        task.setTaskSolver(taskSolver);
        task.setTaskSetDate(LocalDateTime.now());
        task.setCurrentStatus(statusDao.getReferenceById(Status.NOT_STARTED.getId()));
        task.setPriority(priorityDao.getReferenceById(createTaskRequestDTO.taskPriorityId()));
        task = taskDao.save(task);

        return taskMapper.toCreateTaskResponseDTO(task);
    }
}
