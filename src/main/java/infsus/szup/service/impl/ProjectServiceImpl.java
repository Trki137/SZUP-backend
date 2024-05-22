package infsus.szup.service.impl;

import infsus.szup.model.dto.project.ProjectRequestDTO;
import infsus.szup.model.dto.project.ProjectResponseDTO;
import infsus.szup.model.dto.team.TeamRequestDTO;
import infsus.szup.model.entity.ProjectEntity;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectDao projectDao;
    private final UserDao userDao;
    private final TeamService teamService;
    @PersistenceContext
    private final EntityManager em;
    @Override
    public ProjectResponseDTO createProject(final ProjectRequestDTO projectRequestDTO) {
        if (projectDao.existsByProjectName(projectRequestDTO.projectName())){
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

        for (final TeamRequestDTO teamRequestDTO: projectRequestDTO.teams()){
            teamService.createTeam(teamRequestDTO, project.getId());
        }
        return null;
    }
}
