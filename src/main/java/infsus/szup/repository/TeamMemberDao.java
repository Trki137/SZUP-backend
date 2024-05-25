package infsus.szup.repository;

import infsus.szup.model.entity.TeamEntity;
import infsus.szup.model.entity.TeamMemberEntity;
import infsus.szup.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamMemberDao extends JpaRepository<TeamMemberEntity, Long> {
    Optional<TeamMemberEntity> findByTeamMemberAndTeam(UserEntity teamMember, TeamEntity team);
    Boolean existsByTeamLeaderFalse();
}
