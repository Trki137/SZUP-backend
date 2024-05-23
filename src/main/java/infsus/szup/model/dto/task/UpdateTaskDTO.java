package infsus.szup.model.dto.task;

import java.time.LocalDateTime;

public record UpdateTaskDTO(String taskName, LocalDateTime taskEndDate, String description,
                            Integer priorityId) {
}
