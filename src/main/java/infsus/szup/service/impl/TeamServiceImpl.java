package infsus.szup.service.impl;

import infsus.szup.mapper.TeamMapper;
import infsus.szup.model.dto.team.TeamInfoResponseDTO;
import infsus.szup.model.dto.team.TeamRequestDTO;
import infsus.szup.model.dto.team.TeamResponseDTO;
import infsus.szup.model.dto.team.TeamUpdateRequestDTO;
import infsus.szup.model.dto.team.teammember.TeamMemberRequestDTO;
import infsus.szup.model.entity.ProjectEntity;
import infsus.szup.model.entity.TeamEntity;
import infsus.szup.model.entity.TeamMemberEntity;
import infsus.szup.model.entity.UserEntity;
import infsus.szup.repository.ProjectDao;
import infsus.szup.repository.TeamDao;
import infsus.szup.repository.TeamMemberDao;
import infsus.szup.repository.UserDao;
import infsus.szup.service.TeamMemberService;
import infsus.szup.service.TeamService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamDao teamDao;
    private final ProjectDao projectDao;
    private final UserDao userDao;
    private final TeamMemberDao teamMemberDao;
    private final TeamMemberService teamMemberService;
    private final TeamMapper teamMapper;

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    @Override
    public TeamResponseDTO createTeam(TeamRequestDTO teamRequestDTO, Long projectId) {
        final ProjectEntity project = projectDao.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Project with id %d doesn't exists", projectId))
        );

        if (teamDao.existsByTeamNameAndProject(teamRequestDTO.teamName(), project)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Team with name " + teamRequestDTO.teamName() + " already exists");
        }

        TeamEntity team = new TeamEntity();
        team.setTeamName(teamRequestDTO.teamName());
        team.setProject(project);
        team = teamDao.save(team);

        em.flush();
        teamMemberService.createTeamMember(new TeamMemberRequestDTO(teamRequestDTO.teamLeaderId(), true), team.getId());

        em.flush();
        em.refresh(team);
        return teamMapper.toTeamResponseDTO(team);
    }

    @Transactional
    @Override
    public void deleteTeam(Long projectId, Long teamId) {
        final ProjectEntity project = projectDao.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Project with id %d doesn't exists", projectId))
        );

        final TeamEntity team = teamDao.findById(teamId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Team with id %d not found!", teamId))
        );

        if (!team.getProject().equals(project)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Team %s doesn't belong to project %s", team.getTeamName(), project.getProjectName()));
        }

        teamDao.delete(team);
    }

    @Transactional
    @Override
    public TeamResponseDTO updateTeam(TeamUpdateRequestDTO teamUpdateRequestDTO, Long projectId, Long teamId) {
        final ProjectEntity project = projectDao.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Project with id %d doesn't exists", projectId))
        );

        final TeamEntity team = teamDao.findById(teamId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Team with id %d not found!", teamId))
        );

        if (!team.getProject().equals(project)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Team %s doesn't belong to project %s", team.getTeamName(), project.getProjectName()));
        }

        team.setTeamName(teamUpdateRequestDTO.teamName());

        return teamMapper.toTeamResponseDTO(team);
    }

    @Transactional
    @Override
    public TeamResponseDTO addMember(Long projectId, Long teamId, Long memberId, Long addedByUserId) {
        final ProjectEntity project = projectDao.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Project with id %d doesn't exists", projectId))
        );

        final TeamEntity team = teamDao.findById(teamId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Team with id %d not found!", teamId))
        );

        final UserEntity newMember = userDao.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id %d not found!", memberId))
        );

        if (!team.getProject().equals(project)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Team %s doesn't belong to project %s", team.getTeamName(), project.getProjectName()));
        }

        boolean isLeader = team.getTeamMembers()
                .stream()
                .filter(TeamMemberEntity::getTeamLeader)
                .allMatch(member -> member.getTeamMember().getId().equals(addedByUserId));

        if (!isLeader) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only team leader can add new member of a team");
        }

        teamMemberDao.save(new TeamMemberEntity(null, newMember, team, false));
        em.refresh(team);
        return teamMapper.toTeamResponseDTO(team);
    }

    @Transactional
    @Override
    public TeamResponseDTO removeMember(Long projectId, Long teamId, Long memberId, Long removedByUserId) {
        final ProjectEntity project = projectDao.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Project with id %d doesn't exists", projectId))
        );

        final TeamEntity team = teamDao.findById(teamId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Team with id %d not found!", teamId))
        );


        if (!team.getProject().equals(project)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Team %s doesn't belong to project %s", team.getTeamName(), project.getProjectName()));
        }

        boolean isLeader = team.getTeamMembers()
                .stream()
                .filter(TeamMemberEntity::getTeamLeader)
                .allMatch(member -> member.getTeamMember().getId().equals(removedByUserId));

        if (!isLeader) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only team leader can add new member of a team");
        }

        final TeamMemberEntity teamMemberEntity = teamMemberDao.findByTeamMemberAndTeam(userDao.getReferenceById(memberId), team).orElseThrow(
                () -> new EntityNotFoundException(String.format("Team member with id %d doesnt exists", memberId))
        );

        teamMemberDao.delete(teamMemberEntity);
        em.flush();
        em.refresh(team);
        return teamMapper.toTeamResponseDTO(team);
    }

    @Override
    public List<TeamInfoResponseDTO> getTeamsForProject(Long projectId) {
        return projectDao.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Project with id %d not found", projectId))
        ).getTeams().stream().map(teamMapper::toTeamInfoResponseDTO).toList();
    }
}
