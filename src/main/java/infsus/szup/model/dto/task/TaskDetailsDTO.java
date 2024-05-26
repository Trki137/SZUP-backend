package infsus.szup.model.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import infsus.szup.model.dto.priority.PriorityResponseDTO;
import infsus.szup.model.dto.status.StatusResponseDTO;
import infsus.szup.model.dto.users.UserResponseDTO;

import java.time.LocalDateTime;

public record TaskDetailsDTO(
    Long id,
    String taskName,
    Long solverTeamId,
    @JsonProperty("taskSetDate") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime taskSetDate,
    @JsonProperty("taskEndDate") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime taskEndDate,
    String description,
    UserResponseDTO taskOwner,
    UserResponseDTO taskSolver,
    StatusResponseDTO currentStatus,
    PriorityResponseDTO priority) {
}
