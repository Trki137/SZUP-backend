package infsus.szup.repository;

import infsus.szup.model.entity.TeamMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamMemberDao extends JpaRepository<TeamMemberEntity, Long> {
    Optional<TeamMemberEntity> findByTeamMember_Id(Long id);
}
