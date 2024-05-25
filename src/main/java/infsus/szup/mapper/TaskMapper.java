package infsus.szup.mapper;

import infsus.szup.model.dto.priority.PriorityResponseDTO;
import infsus.szup.model.dto.status.StatusResponseDTO;
import infsus.szup.model.dto.task.CreateTaskRequestDTO;
import infsus.szup.model.dto.task.TaskDetailsDTO;
import infsus.szup.model.dto.task.TaskResponseDTO;
import infsus.szup.model.dto.task.UpdateTaskDTO;
import infsus.szup.model.dto.users.UserResponseDTO;
import infsus.szup.model.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {UserMapper.class, StatusMapper.class, PriorityMapper.class})
public abstract class TaskMapper {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StatusMapper statusMapper;
    @Autowired
    private PriorityMapper priorityMapper;

    public abstract TaskResponseDTO toTaskResponseDTO(TaskEntity task);

    public abstract TaskEntity toTaskEntity(CreateTaskRequestDTO createTaskRequestDTO);

    public TaskDetailsDTO toTaskDetails(TaskEntity taskEntity) {
        if (taskEntity == null) {
            return null;
        }

        Long id = null;
        String taskName = null;
        Long teamId = null;
        LocalDateTime taskSetDate = null;
        LocalDateTime taskEndDate = null;
        String description = null;
        UserResponseDTO taskOwner = null;
        UserResponseDTO taskSolver = null;
        StatusResponseDTO currentStatus = null;
        PriorityResponseDTO priority = null;

        id = taskEntity.getId();
        taskName = taskEntity.getTaskName();
        teamId = taskEntity.getProject().getTeams().stream()
                .flatMap(team -> team.getTeamMembers().stream())
                .filter(teamMemberEntity -> teamMemberEntity.getTeamMember().getId().equals(taskEntity.getTaskSolver().getId()))
                .map(teamMemberEntity -> teamMemberEntity.getTeam().getId()).findFirst()
                .orElse(null);
        taskSetDate = taskEntity.getTaskSetDate();
        taskEndDate = taskEntity.getTaskEndDate();
        description = taskEntity.getDescription();
        taskOwner = userMapper.toUserResponseDTO(taskEntity.getTaskOwner());
        taskSolver = userMapper.toUserResponseDTO(taskEntity.getTaskSolver());
        currentStatus = statusMapper.toStatusResponseDTO(taskEntity.getCurrentStatus());
        priority = priorityMapper.toPriorityResponseDTO(taskEntity.getPriority());

        TaskDetailsDTO taskDetailsDTO = new TaskDetailsDTO(id, taskName, teamId, taskSetDate, taskEndDate, description, taskOwner, taskSolver, currentStatus, priority);

        return taskDetailsDTO;
    }

    public abstract void updateTask(@MappingTarget TaskEntity taskEntity, UpdateTaskDTO updateTaskDTO);
}
