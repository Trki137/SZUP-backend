package infsus.szup.repository;

import infsus.szup.model.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDao extends JpaRepository<TaskEntity, Long> {
}
