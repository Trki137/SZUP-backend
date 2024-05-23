package infsus.szup.service.impl;

import infsus.szup.mapper.PriorityMapper;
import infsus.szup.model.dto.priority.PriorityResponseDTO;
import infsus.szup.repository.PriorityDao;
import infsus.szup.service.PriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriorityServiceImpl implements PriorityService {
    private final PriorityMapper priorityMapper;
    private final PriorityDao priorityDao;
    @Override
    public List<PriorityResponseDTO> getAllPriorities() {
        return priorityDao.findAll().stream().map(priorityMapper::toPriorityResponseDTO).toList();
    }
}
