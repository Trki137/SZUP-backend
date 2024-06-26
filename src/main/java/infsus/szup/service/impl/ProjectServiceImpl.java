package infsus.szup.service.impl;

import infsus.szup.mapper.TeamMapper;
import infsus.szup.model.dto.project.ProjectDetailsResponseDTO;
import infsus.szup.model.dto.project.ProjectRequestDTO;
import infsus.szup.model.dto.project.ProjectResponseDTO;
import infsus.szup.model.dto.project.UpdateProjectReqDTO;
import infsus.szup.model.dto.rights.UserRightsResponseDTO;
import infsus.szup.model.dto.team.TeamRequestDTO;
import infsus.szup.model.dto.team.TeamResponseDTO;
import infsus.szup.model.entity.ProjectEntity;
import infsus.szup.model.entity.TeamEntity;
import infsus.szup.model.entity.UserEntity;
import infsus.szup.repository.ProjectDao;
import infsus.szup.repository.UserDao;
import infsus.szup.service.ProjectService;
import infsus.szup.service.TeamService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectDao projectDao;
    private final UserDao userDao;
    private final TeamService teamService;
    private final TeamMapper teamMapper;
    @PersistenceContext
    private final EntityManager em;

    @Transactional
    @Override
    public ProjectResponseDTO createProject(final ProjectRequestDTO projectRequestDTO) {
        if (projectDao.existsByProjectName(projectRequestDTO.projectName())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Project name taken!");
        }

        final UserEntity user = userDao.findById(projectRequestDTO.createdById()).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id %d not found!", projectRequestDTO.createdById()))
        );

        ProjectEntity project = new ProjectEntity();
        project.setCreatedBy(user);
        project.setProjectName(projectRequestDTO.projectName());
        project.setCreationDate(LocalDateTime.now());
        project = projectDao.save(project);

        em.flush();

        for (final TeamRequestDTO teamRequestDTO : projectRequestDTO.teams()) {
            teamService.createTeam(teamRequestDTO, project.getId());
        }
        return new ProjectResponseDTO(project.getId(), project.getProjectName());
    }

    @Transactional
    @Override
    public ProjectResponseDTO updateProject(final Long projectId, final UpdateProjectReqDTO updateProjectReqDTO) {
        final ProjectEntity project = projectDao.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Project with id %d doesn't exists", projectId))
        );

        if (projectDao.existsByProjectName(updateProjectReqDTO.projectName())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Project name taken!");
        }

        project.setProjectName(updateProjectReqDTO.projectName());

        return new ProjectResponseDTO(project.getId(), project.getProjectName());
    }

    @Transactional
    @Override
    public void deleteProject(Long projectId) {
        final ProjectEntity project = projectDao.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Project with id %d doesn't exists", projectId))
        );

        projectDao.delete(project);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectResponseDTO> getAllProjectsForUser(Long userId) {
        return projectDao.getAllProjectsForUser(userId).stream().map(project -> new ProjectResponseDTO(project
                .getId(), project.getProjectName())).toList();
    }

    @Override
    public ProjectDetailsResponseDTO getProjectDetails(Long projectId, Long userId) {
        final ProjectEntity project = projectDao.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Project with id %d doesn't exists", projectId))
        );

        boolean isProjectOwner = project.getCreatedBy().getId().equals(userId);
        TeamEntity teamOwner = project.getTeams().stream()
                .filter(team -> team.getTeamMembers().stream()
                        .anyMatch(teamMemberEntity -> teamMemberEntity.getTeamLeader() && teamMemberEntity.getTeamMember().getId().equals(userId)))
                .findFirst()
                .orElse(null);
        final boolean isTeamLeader = teamOwner != null;

        UserRightsResponseDTO userRightsResponseDTO = new UserRightsResponseDTO(isProjectOwner, isTeamLeader);
        return toProjectDetailsResponseDTO(project, userRightsResponseDTO, teamOwner);
    }

    private ProjectDetailsResponseDTO toProjectDetailsResponseDTO(ProjectEntity project, UserRightsResponseDTO userRightsResponseDTO, TeamEntity teamOwner) {
        if (project == null) {
            return null;
        }

        Long id = project.getId();
        String projectName = project.getProjectName();
        List<TeamResponseDTO> teams = userRightsResponseDTO.canModifyProject() ? project.getTeams().stream().map(teamMapper::toTeamResponseDTO).toList() : null;

        TeamResponseDTO teamLeaderOf = teamMapper.toTeamResponseDTO(teamOwner);

        return new ProjectDetailsResponseDTO(id, projectName, userRightsResponseDTO, teams, teamLeaderOf);
    }

}
