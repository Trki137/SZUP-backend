package infsus.szup.service.unit.service;

import infsus.szup.model.dto.team.teammember.TeamMemberRequestDTO;
import infsus.szup.model.entity.TeamEntity;
import infsus.szup.model.entity.TeamMemberEntity;
import infsus.szup.model.entity.UserEntity;
import infsus.szup.repository.TeamDao;
import infsus.szup.repository.TeamMemberDao;
import infsus.szup.repository.UserDao;
import infsus.szup.service.impl.TeamMemberServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TeamMemberServiceImplTest {

    @Mock
    private TeamDao teamDao;

    @Mock
    private UserDao userDao;

    @Mock
    private TeamMemberDao teamMemberDao;

    @InjectMocks
    private TeamMemberServiceImpl teamMemberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTeamMember_Success() {
        TeamMemberRequestDTO teamMemberRequestDTO = new TeamMemberRequestDTO(1L, true);
        TeamEntity teamEntity = new TeamEntity();
        UserEntity userEntity = new UserEntity();

        when(teamDao.findById(anyLong())).thenReturn(java.util.Optional.of(teamEntity));
        when(userDao.findById(anyLong())).thenReturn(java.util.Optional.of(userEntity));

        teamMemberService.createTeamMember(teamMemberRequestDTO, 1L);

        verify(teamDao, times(1)).findById(anyLong());
        verify(userDao, times(1)).findById(anyLong());
        verify(teamMemberDao, times(1)).save(any(TeamMemberEntity.class));
        verifyNoMoreInteractions(teamDao);
        verifyNoMoreInteractions(userDao);
        verifyNoMoreInteractions(teamMemberDao);
    }

    @Test
    void testCreateTeamMember_TeamNotFound() {
        TeamMemberRequestDTO teamMemberRequestDTO = new TeamMemberRequestDTO(1L, true);

        when(teamDao.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamMemberService.createTeamMember(teamMemberRequestDTO, 1L));

        verify(teamDao, times(1)).findById(anyLong());
        verifyNoMoreInteractions(teamDao);
        verifyNoMoreInteractions(userDao);
        verifyNoMoreInteractions(teamMemberDao);
    }

    @Test
    void testCreateTeamMember_UserNotFound() {
        TeamMemberRequestDTO teamMemberRequestDTO = new TeamMemberRequestDTO(1L, true);
        TeamEntity teamEntity = new TeamEntity();

        when(teamDao.findById(anyLong())).thenReturn(java.util.Optional.of(teamEntity));
        when(userDao.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamMemberService.createTeamMember(teamMemberRequestDTO, 1L));

        verify(teamDao, times(1)).findById(anyLong());
        verify(userDao, times(1)).findById(anyLong());
        verifyNoMoreInteractions(teamDao);
        verifyNoMoreInteractions(userDao);
        verifyNoMoreInteractions(teamMemberDao);
    }
}

