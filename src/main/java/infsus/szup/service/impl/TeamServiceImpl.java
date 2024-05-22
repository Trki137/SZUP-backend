package infsus.szup.service.impl;

import infsus.szup.model.dto.team.TeamRequestDTO;
import infsus.szup.model.dto.team.teammember.TeamMemberRequestDTO;
import infsus.szup.model.entity.ProjectEntity;
import infsus.szup.model.entity.TeamEntity;
import infsus.szup.model.entity.TeamMemberEntity;
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

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamDao teamDao;
    private final ProjectDao projectDao;
    private final TeamMemberService teamMemberService;
    @PersistenceContext
    private final EntityManager em;

    @Transactional
    @Override
    public void createTeam(TeamRequestDTO teamRequestDTO, Long projectId) {
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

        for (TeamMemberRequestDTO teamMemberRequestDTO: teamRequestDTO.teamMembers()){
            teamMemberService.createTeamMember(teamMemberRequestDTO, team.getId());
        }
    }
}
