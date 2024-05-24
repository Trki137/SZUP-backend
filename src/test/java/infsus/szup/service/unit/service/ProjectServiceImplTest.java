package infsus.szup.service.unit.service;

import infsus.szup.mapper.TeamMapper;
import infsus.szup.model.dto.project.ProjectDetailsResponseDTO;
import infsus.szup.model.dto.project.ProjectRequestDTO;
import infsus.szup.model.dto.project.ProjectResponseDTO;
import infsus.szup.model.dto.project.UpdateProjectReqDTO;
import infsus.szup.model.dto.team.TeamResponseDTO;
import infsus.szup.model.entity.ProjectEntity;
import infsus.szup.model.entity.TeamEntity;
import infsus.szup.model.entity.UserEntity;
import infsus.szup.repository.ProjectDao;
import infsus.szup.repository.UserDao;
import infsus.szup.service.impl.ProjectServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ProjectServiceImplTest {

    @Mock
    private ProjectDao projectDao;

    @Mock
    private UserDao userDao;

    @Mock
    private TeamMapper teamMapper;

    @Mock
    private EntityManager em;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProject_Success() {
        ProjectRequestDTO projectRequestDTO = new ProjectRequestDTO("Project A", 1L, new ArrayList<>());
        UserEntity userEntity = new UserEntity();
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setProjectName("Project A");
        projectEntity.setCreatedBy(userEntity);
        projectEntity.setCreationDate(LocalDateTime.now());

        when(projectDao.existsByProjectName(any(String.class))).thenReturn(false);
        when(userDao.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(projectDao.save(any(ProjectEntity.class))).thenReturn(projectEntity);

        ProjectResponseDTO result = projectService.createProject(projectRequestDTO);

        assertEquals("Project A", result.projectName());

        verify(projectDao, times(1)).existsByProjectName(any(String.class));
        verify(userDao, times(1)).findById(anyLong());
        verify(projectDao, times(1)).save(any(ProjectEntity.class));
        verifyNoMoreInteractions(projectDao);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void testCreateProject_ProjectNameTaken() {
        ProjectRequestDTO projectRequestDTO = new ProjectRequestDTO("Project A", 1L, new ArrayList<>());

        when(projectDao.existsByProjectName(any(String.class))).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> projectService.createProject(projectRequestDTO));

        verify(projectDao, times(1)).existsByProjectName(any(String.class));
        verifyNoMoreInteractions(projectDao);
    }

    @Test
    void testCreateProject_UserNotFound() {
        ProjectRequestDTO projectRequestDTO = new ProjectRequestDTO("Project A", 1L, new ArrayList<>());

        when(projectDao.existsByProjectName(any(String.class))).thenReturn(false);
        when(userDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> projectService.createProject(projectRequestDTO));

        verify(projectDao, times(1)).existsByProjectName(any(String.class));
        verify(userDao, times(1)).findById(anyLong());
        verifyNoMoreInteractions(projectDao);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void testUpdateProject_Success() {
        UpdateProjectReqDTO updateProjectReqDTO = new UpdateProjectReqDTO("Updated Project");
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setProjectName("Original Project");

        when(projectDao.findById(anyLong())).thenReturn(Optional.of(projectEntity));
        when(projectDao.existsByProjectName(any(String.class))).thenReturn(false);

        ProjectResponseDTO result = projectService.updateProject(1L, updateProjectReqDTO);

        assertEquals("Updated Project", result.projectName());

        verify(projectDao, times(1)).findById(anyLong());
        verify(projectDao, times(1)).existsByProjectName(any(String.class));
        verifyNoMoreInteractions(projectDao);
    }

    @Test
    void testUpdateProject_ProjectNotFound() {
        UpdateProjectReqDTO updateProjectReqDTO = new UpdateProjectReqDTO("Updated Project");

        when(projectDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> projectService.updateProject(1L, updateProjectReqDTO));

        verify(projectDao, times(1)).findById(anyLong());
        verifyNoMoreInteractions(projectDao);
    }

    @Test
    void testUpdateProject_ProjectNameTaken() {
        UpdateProjectReqDTO updateProjectReqDTO = new UpdateProjectReqDTO("Updated Project");
        ProjectEntity projectEntity = new ProjectEntity();

        when(projectDao.findById(anyLong())).thenReturn(Optional.of(projectEntity));
        when(projectDao.existsByProjectName(any(String.class))).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> projectService.updateProject(1L, updateProjectReqDTO));

        verify(projectDao, times(1)).findById(anyLong());
        verify(projectDao, times(1)).existsByProjectName(any(String.class));
        verifyNoMoreInteractions(projectDao);
    }

    // Test for deleteProject method
    @Test
    void testDeleteProject_Success() {
        ProjectEntity projectEntity = new ProjectEntity();

        when(projectDao.findById(anyLong())).thenReturn(Optional.of(projectEntity));

        projectService.deleteProject(1L);

        verify(projectDao, times(1)).findById(anyLong());
        verify(projectDao, times(1)).delete(any(ProjectEntity.class));
        verifyNoMoreInteractions(projectDao);
    }

    @Test
    void testDeleteProject_ProjectNotFound() {
        when(projectDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> projectService.deleteProject(1L));

        verify(projectDao, times(1)).findById(anyLong());
        verifyNoMoreInteractions(projectDao);
    }

    @Test
    void testGetAllProjectsForUser_Success() {
        ProjectEntity project1 = new ProjectEntity();
        project1.setId(1L);
        project1.setProjectName("Project 1");

        ProjectEntity project2 = new ProjectEntity();
        project2.setId(2L);
        project2.setProjectName("Project 2");

        when(projectDao.getAllProjectsForUser(anyLong())).thenReturn(List.of(project1, project2));

        List<ProjectResponseDTO> result = projectService.getAllProjectsForUser(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Project 1", result.get(0).projectName());
        assertEquals("Project 2", result.get(1).projectName());

        verify(projectDao, times(1)).getAllProjectsForUser(anyLong());
        verifyNoMoreInteractions(projectDao);
    }

    @Test
    void testGetAllProjectsForUser_EmptyList() {
        when(projectDao.getAllProjectsForUser(anyLong())).thenReturn(Collections.emptyList());

        List<ProjectResponseDTO> result = projectService.getAllProjectsForUser(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(projectDao, times(1)).getAllProjectsForUser(anyLong());
        verifyNoMoreInteractions(projectDao);
    }

    @Test
    void testGetProjectDetails_Success() {
        Long projectId = 1L;
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);

        ProjectEntity projectEntity = new ProjectEntity(projectId, "Project", LocalDateTime.now(), userEntity, null);
        projectEntity.setCreatedBy(userEntity);

        TeamEntity teamEntity = new TeamEntity(1L, "Team", projectEntity,Collections.emptyList());
        projectEntity.setTeams(List.of(teamEntity));

        when(projectDao.findById(anyLong())).thenReturn(Optional.of(projectEntity));
        when(teamMapper.toTeamResponseDTO(any(TeamEntity.class))).thenReturn(new TeamResponseDTO(1L, "Team", Collections.emptyList()));

        ProjectDetailsResponseDTO result = projectService.getProjectDetails(projectId, userId);

        assertNotNull(result);
        assertEquals("Project", result.projectName());
        assertTrue(result.userRightsResponseDTO().canModifyProject());
        assertNotNull(result.teams());
        assertEquals(1, result.teams().size());
        assertEquals("Team", result.teams().get(0).teamName());

        verify(projectDao, times(1)).findById(anyLong());
        verify(teamMapper, times(1)).toTeamResponseDTO(any(TeamEntity.class));
        verifyNoMoreInteractions(projectDao);
    }

    @Test
    void testGetProjectDetails_ProjectNotFound() {
        when(projectDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> projectService.getProjectDetails(1L, 1L));

        verify(projectDao, times(1)).findById(anyLong());
        verifyNoMoreInteractions(projectDao, teamMapper);
    }
}

