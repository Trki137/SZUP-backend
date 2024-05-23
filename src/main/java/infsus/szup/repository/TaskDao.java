package infsus.szup.repository;

import infsus.szup.model.entity.TaskEntity;
import infsus.szup.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskDao extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByTaskSolver(UserEntity taskSolver);
    List<TaskEntity> findByTaskOwner(UserEntity taskOwner);
}
