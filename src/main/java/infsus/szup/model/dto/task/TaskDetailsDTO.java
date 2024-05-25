package infsus.szup.model.dto.task;

import infsus.szup.model.dto.priority.PriorityResponseDTO;
import infsus.szup.model.dto.status.StatusResponseDTO;
import infsus.szup.model.dto.users.UserResponseDTO;

import java.time.LocalDateTime;

public record TaskDetailsDTO(Long id, String taskName, Long solverTeamId, LocalDateTime taskSetDate,
                             LocalDateTime taskEndDate,
                             String description, UserResponseDTO taskOwner, UserResponseDTO taskSolver,
                             StatusResponseDTO currentStatus, PriorityResponseDTO priority) {
}
