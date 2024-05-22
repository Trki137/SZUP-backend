package infsus.szup.service;

import infsus.szup.mapper.TeamMapper;
import infsus.szup.model.dto.team.TeamRequestDTO;
import infsus.szup.model.dto.team.TeamResponseDTO;
import infsus.szup.model.dto.team.TeamUpdateRequestDTO;
import infsus.szup.model.entity.ProjectEntity;
import infsus.szup.model.entity.TeamEntity;
import infsus.szup.model.entity.TeamMemberEntity;
import infsus.szup.model.entity.UserEntity;
import infsus.szup.repository.ProjectDao;
import infsus.szup.repository.TeamDao;
import infsus.szup.repository.TeamMemberDao;
import infsus.szup.repository.UserDao;
import infsus.szup.service.impl.TeamServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TeamServiceImplTest {

    @Mock
    private TeamDao teamDao;


    @Mock
    private TeamMemberDao teamMemberDao;

    @Mock
    private ProjectDao projectDao;

    @Mock
    private UserDao userDao;

    @Mock
    private TeamMapper teamMapper;

    @Mock
    private EntityManager em;

    @InjectMocks
    private TeamServiceImpl teamService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for createTeam method
    @Test
    void testCreateTeam_Success() {
        TeamRequestDTO teamRequestDTO = new TeamRequestDTO("Team A", Collections.emptyList());
        ProjectEntity projectEntity = new ProjectEntity(1L, "Project A", LocalDateTime.now(), new UserEntity());

        when(projectDao.findById(anyLong())).thenReturn(java.util.Optional.of(projectEntity));
        when(teamDao.existsByTeamNameAndProject(anyString(), any(ProjectEntity.class))).thenReturn(false);
        when(teamDao.save(any(TeamEntity.class))).thenReturn(new TeamEntity());
        when(teamMapper.toTeamResponseDTO(any(TeamEntity.class))).thenReturn(new TeamResponseDTO(1L, "Team A", Collections.emptyList()));

        teamService.createTeam(teamRequestDTO, 1L);

        verify(projectDao, times(1)).findById(anyLong());
        verify(teamDao, times(1)).existsByTeamNameAndProject(anyString(), any());
        verify(teamDao, times(1)).save(any(TeamEntity.class));
        verify(teamMapper, times(1)).toTeamResponseDTO(any(TeamEntity.class));
        verifyNoMoreInteractions(teamMapper);
        verifyNoMoreInteractions(teamDao);
        verifyNoMoreInteractions(projectDao);
    }

    @Test
    void testCreateTeam_ProjectNotFound() {
        when(projectDao.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            teamService.createTeam(new TeamRequestDTO("Team A", Collections.emptyList()), 1L);
        });

        verify(projectDao, times(1)).findById(anyLong());
        verifyNoMoreInteractions(projectDao);
    }

    @Test
    void testCreateTeam_TeamAlreadyExists() {
        TeamRequestDTO teamRequestDTO = new TeamRequestDTO("Team A", Collections.emptyList());
        ProjectEntity projectEntity = new ProjectEntity(1L, "Project A", LocalDateTime.now(), new UserEntity());

        when(projectDao.findById(anyLong())).thenReturn(java.util.Optional.of(projectEntity));
        when(teamDao.existsByTeamNameAndProject(anyString(), any(ProjectEntity.class))).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> {
            teamService.createTeam(teamRequestDTO, 1L);
        });

        verify(projectDao, times(1)).findById(anyLong());
        verify(teamDao, times(1)).existsByTeamNameAndProject(anyString(), any(ProjectEntity.class));
        verifyNoMoreInteractions(projectDao);
        verifyNoMoreInteractions(teamDao);
    }

    // Test for updateTeam method
    @Test
    void testUpdateTeam_Success() {
        TeamUpdateRequestDTO teamUpdateRequestDTO = new TeamUpdateRequestDTO("Updated Team");
        ProjectEntity projectEntity = new ProjectEntity();
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setProject(projectEntity);

        when(projectDao.findById(anyLong())).thenReturn(java.util.Optional.of(projectEntity));
        when(teamDao.findById(anyLong())).thenReturn(java.util.Optional.of(teamEntity));
        when(teamMapper.toTeamResponseDTO(any(TeamEntity.class))).thenReturn(new TeamResponseDTO(1L, "Updated Team", Collections.emptyList()));

        teamService.updateTeam(teamUpdateRequestDTO, 1L, 1L);

        verify(projectDao, times(1)).findById(anyLong());
        verify(teamDao, times(1)).findById(anyLong());
        verify(teamMapper, times(1)).toTeamResponseDTO(any(TeamEntity.class));
        verifyNoMoreInteractions(projectDao);
        verifyNoMoreInteractions(teamDao);
        verifyNoMoreInteractions(teamMapper);
    }

    @Test
    void testUpdateTeam_ProjectNotFound() {
        when(projectDao.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            teamService.updateTeam(new TeamUpdateRequestDTO("Updated Team"), 1L, 1L);
        });

        verify(projectDao, times(1)).findById(anyLong());
        verifyNoMoreInteractions(projectDao);
    }

    @Test
    void testUpdateTeam_TeamNotFound() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(1L);

        when(projectDao.findById(anyLong())).thenReturn(java.util.Optional.of(projectEntity));
        when(teamDao.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            teamService.updateTeam(new TeamUpdateRequestDTO("Updated Team"), 1L, 1L);
        });

        verify(projectDao, times(1)).findById(anyLong());
        verify(teamDao, times(1)).findById(anyLong());
        verifyNoMoreInteractions(projectDao);
        verifyNoMoreInteractions(teamDao);
    }

    // Test for addMember method
    @Test
    void testAddMember_Success() {
        ProjectEntity projectEntity = new ProjectEntity();
        TeamEntity teamEntity = new TeamEntity();
        UserEntity userEntity = new UserEntity(1L, "Dean", "Trkulja", "dean@gmail.com", "123");
        TeamMemberEntity teamMemberEntity = new TeamMemberEntity(1L, userEntity, teamEntity, true);
        teamEntity.setProject(projectEntity);
        teamEntity.setTeamMembers(List.of(teamMemberEntity));

        when(projectDao.findById(anyLong())).thenReturn(java.util.Optional.of(projectEntity));
        when(teamDao.findById(anyLong())).thenReturn(java.util.Optional.of(teamEntity));
        when(userDao.findById(anyLong())).thenReturn(java.util.Optional.of(userEntity));
        when(teamMemberDao.save(any())).thenReturn(teamMemberEntity);

        teamService.addMember(1L, 1L, 1L, 1L);

        verify(projectDao, times(1)).findById(anyLong());
        verify(teamDao, times(1)).findById(anyLong());
        verify(userDao, times(1)).findById(anyLong());
        verify(teamMemberDao, times(1)).save(any());
        verifyNoMoreInteractions(projectDao);
        verifyNoMoreInteractions(teamDao);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void testAddMember_ProjectNotFound() {
        when(projectDao.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            teamService.addMember(1L, 1L, 1L, 1L);
        });

        verify(projectDao, times(1)).findById(anyLong());
        verifyNoMoreInteractions(projectDao);
    }

    @Test
    void testAddMember_TeamNotFound() {
        ProjectEntity projectEntity = new ProjectEntity();

        when(projectDao.findById(anyLong())).thenReturn(java.util.Optional.of(projectEntity));
        when(teamDao.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            teamService.addMember(1L, 1L, 1L, 1L);
        });

        verify(projectDao, times(1)).findById(anyLong());
        verify(teamDao, times(1)).findById(anyLong());
        verifyNoMoreInteractions(projectDao);
        verifyNoMoreInteractions(teamDao);
    }

    @Test
    void testAddMember_UserNotFound() {
        ProjectEntity projectEntity = new ProjectEntity();
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setProject(projectEntity);

        when(projectDao.findById(anyLong())).thenReturn(java.util.Optional.of(projectEntity));
        when(teamDao.findById(anyLong())).thenReturn(java.util.Optional.of(teamEntity));
        when(userDao.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            teamService.addMember(1L, 1L, 1L, 1L);
        });

        verify(projectDao, times(1)).findById(anyLong());
        verify(teamDao, times(1)).findById(anyLong());
        verify(userDao, times(1)).findById(anyLong());
        verifyNoMoreInteractions(projectDao);
        verifyNoMoreInteractions(teamDao);
        verifyNoMoreInteractions(userDao);
    }
}
