package infsus.szup.service;

import infsus.szup.model.dto.status.StatusResponseDTO;

import java.util.List;

public interface StatusService {
    List<StatusResponseDTO> getAllStatuses();
}
