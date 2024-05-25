package infsus.szup.repository;

import infsus.szup.model.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectDao extends JpaRepository<ProjectEntity, Long> {
    boolean existsByProjectName(String projectName);

    String GET_PROJECTS_FOR_USER_QUERY = """
            SELECT DISTINCT
                project.*
            FROM team_member
            JOIN team ON team_member.team_id = team.team_id
            JOIN project ON team.project_id = project.project_id
            WHERE team_member.user_id = :userId or project.user_id = :userId
                        
            """;
    @Query(value = GET_PROJECTS_FOR_USER_QUERY, nativeQuery = true)
    List<ProjectEntity> getAllProjectsForUser(Long userId);
}
