package infsus.szup.repository;

import infsus.szup.model.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentDao extends JpaRepository<CommentEntity, Long> {
}
