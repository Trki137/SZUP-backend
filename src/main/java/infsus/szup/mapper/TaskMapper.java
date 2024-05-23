package infsus.szup.mapper;

import infsus.szup.model.dto.task.CreateTaskRequestDTO;
import infsus.szup.model.dto.task.TaskResponseDTO;
import infsus.szup.model.dto.task.TaskDetailsDTO;
import infsus.szup.model.dto.task.UpdateTaskDTO;
import infsus.szup.model.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {UserMapper.class, StatusMapper.class, PriorityMapper.class})
public interface TaskMapper {
    TaskResponseDTO toTaskResponseDTO(TaskEntity task);
    TaskEntity toTaskEntity(CreateTaskRequestDTO createTaskRequestDTO);

    TaskDetailsDTO toTaskDetails(TaskEntity taskEntity);

    void updateTask(@MappingTarget TaskEntity taskEntity, UpdateTaskDTO updateTaskDTO);
}
