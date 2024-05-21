package infsus.szup.repository;

import infsus.szup.model.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusDao extends JpaRepository<StatusEntity, Integer> {
}
