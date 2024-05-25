package infsus.szup.repository;

import infsus.szup.model.entity.TeamEntity;
import infsus.szup.model.entity.TeamMemberEntity;
import infsus.szup.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeamMemberDao extends JpaRepository<TeamMemberEntity, Long> {
    Optional<TeamMemberEntity> findByTeamMemberAndTeam(UserEntity teamMember, TeamEntity team);

    @Modifying
    @Query(value = "DELETE FROM team_member where team_member_id = :teamMemberId", nativeQuery = true)
    void deleteTeamMember(Long teamMemberId);
}
