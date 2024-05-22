package infsus.szup.repository;

import infsus.szup.model.entity.ProjectEntity;
import infsus.szup.model.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamDao extends JpaRepository<TeamEntity, Long> {
    boolean existsByTeamNameAndProject(String teamName, ProjectEntity project);
}
