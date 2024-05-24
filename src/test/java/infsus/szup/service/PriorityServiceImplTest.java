package infsus.szup.service;

import infsus.szup.mapper.PriorityMapper;
import infsus.szup.model.dto.priority.PriorityResponseDTO;
import infsus.szup.model.entity.PriorityEntity;
import infsus.szup.repository.PriorityDao;
import infsus.szup.service.impl.PriorityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PriorityServiceImplTest {

    @Mock
    private PriorityDao priorityDao;

    @Mock
    private PriorityMapper priorityMapper;

    @InjectMocks
    private PriorityServiceImpl priorityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPriorities_Success() {
        PriorityEntity priorityEntity1 = new PriorityEntity(1, (short)1, "Kritično");
        PriorityEntity priorityEntity2 = new PriorityEntity(2, (short)2, "Visok");
        PriorityResponseDTO priorityResponseDTO1 = new PriorityResponseDTO(1, (short)1, "Kritično");
        PriorityResponseDTO priorityResponseDTO2 = new PriorityResponseDTO(2, (short)2, "Visok");

        when(priorityDao.findAll()).thenReturn(Arrays.asList(priorityEntity1, priorityEntity2));
        when(priorityMapper.toPriorityResponseDTO(priorityEntity1)).thenReturn(priorityResponseDTO1);
        when(priorityMapper.toPriorityResponseDTO(priorityEntity2)).thenReturn(priorityResponseDTO2);

        List<PriorityResponseDTO> result = priorityService.getAllPriorities();

        assertEquals(2, result.size());
        assertEquals("Kritično", result.get(0).priorityName());
        assertEquals("Visok", result.get(1).priorityName());

        verify(priorityDao, times(1)).findAll();
        verify(priorityMapper, times(1)).toPriorityResponseDTO(priorityEntity1);
        verify(priorityMapper, times(1)).toPriorityResponseDTO(priorityEntity2);
        verifyNoMoreInteractions(priorityDao);
        verifyNoMoreInteractions(priorityMapper);
    }
}
