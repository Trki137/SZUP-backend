package infsus.szup.repository;

import infsus.szup.model.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectDao extends JpaRepository<ProjectEntity, Long> {
    boolean existsByProjectName(String projectName);
}
