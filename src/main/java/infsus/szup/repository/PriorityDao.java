package infsus.szup.repository;

import infsus.szup.model.entity.PriorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorityDao extends JpaRepository<PriorityEntity, Integer> {
}
