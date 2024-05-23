package infsus.szup.mapper;

import infsus.szup.model.dto.task.CreateTaskRequestDTO;
import infsus.szup.model.dto.task.CreateTaskResponseDTO;
import infsus.szup.model.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TaskMapper {
    CreateTaskResponseDTO toCreateTaskResponseDTO(TaskEntity task);
    TaskEntity toTaskEntity(CreateTaskRequestDTO createTaskRequestDTO);
}
