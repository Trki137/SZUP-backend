package infsus.szup.model.dto.task;

public record UpdateTaskStatusDTO(Long taskId, Long updatedByUserId, Integer newTaskStatusId) {
}
