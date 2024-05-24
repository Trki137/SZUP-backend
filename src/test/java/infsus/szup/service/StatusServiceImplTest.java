package infsus.szup.service;

import infsus.szup.mapper.StatusMapper;
import infsus.szup.model.dto.status.StatusResponseDTO;
import infsus.szup.model.entity.StatusEntity;
import infsus.szup.repository.StatusDao;
import infsus.szup.service.impl.StatusServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StatusServiceImplTest {

    @Mock
    private StatusDao statusDao;

    @Mock
    private StatusMapper statusMapper;

    @InjectMocks
    private StatusServiceImpl statusService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStatuses_Success() {
        StatusEntity statusEntity1 = new StatusEntity(1, "Nije zapoceto");
        StatusEntity statusEntity2 = new StatusEntity(2, "U tijeku");
        StatusResponseDTO statusResponseDTO1 = new StatusResponseDTO(1, "Nije zapoceto");
        StatusResponseDTO statusResponseDTO2 = new StatusResponseDTO(2, "U tijeku");

        when(statusDao.findAll()).thenReturn(Arrays.asList(statusEntity1, statusEntity2));
        when(statusMapper.toStatusResponseDTO(statusEntity1)).thenReturn(statusResponseDTO1);
        when(statusMapper.toStatusResponseDTO(statusEntity2)).thenReturn(statusResponseDTO2);

        List<StatusResponseDTO> result = statusService.getAllStatuses();

        assertEquals(2, result.size());
        assertEquals("Nije zapoceto", result.get(0).statusName());
        assertEquals("U tijeku", result.get(1).statusName());

        verify(statusDao, times(1)).findAll();
        verify(statusMapper, times(1)).toStatusResponseDTO(statusEntity1);
        verify(statusMapper, times(1)).toStatusResponseDTO(statusEntity2);
        verifyNoMoreInteractions(statusDao);
        verifyNoMoreInteractions(statusMapper);
    }
}

