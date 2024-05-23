package infsus.szup.service;

import infsus.szup.model.dto.priority.PriorityResponseDTO;

import java.util.List;

public interface PriorityService {
    List<PriorityResponseDTO> getAllPriorities();
}
