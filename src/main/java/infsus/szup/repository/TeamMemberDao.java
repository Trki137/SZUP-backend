package infsus.szup.repository;

import infsus.szup.model.entity.TeamMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberDao extends JpaRepository<TeamMemberEntity, Long> {

}
