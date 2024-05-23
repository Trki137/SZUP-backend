package infsus.szup.model.dto.task;

import java.time.LocalDateTime;

public record CreateTaskRequestDTO(String taskName, LocalDateTime taskEndDate, String description, Short numberOfHours, Long taskOwnerId, Long taskSolverId, Integer taskPriorityId) {

}
