package infsus.szup.model.dto.task;

import java.util.List;

public record UserTasksDTO(List<TaskResponseDTO> taskToSolve, List<TaskResponseDTO> taskOwner) {
}
