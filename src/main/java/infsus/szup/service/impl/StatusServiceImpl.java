package infsus.szup.service.impl;

import infsus.szup.mapper.StatusMapper;
import infsus.szup.model.dto.status.StatusResponseDTO;
import infsus.szup.repository.StatusDao;
import infsus.szup.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StatusServiceImpl implements StatusService {
    private final StatusDao statusDao;
    private final StatusMapper statusMapper;

    @Override
    public List<StatusResponseDTO> getAllStatuses() {
        return statusDao.findAll().stream().map(statusMapper::toStatusResponseDTO).toList();
    }
}
