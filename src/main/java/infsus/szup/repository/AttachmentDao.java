package infsus.szup.repository;

import infsus.szup.model.entity.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentDao extends JpaRepository<AttachmentEntity, Long> {
}
