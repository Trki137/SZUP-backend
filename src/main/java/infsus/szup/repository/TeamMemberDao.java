package infsus.szup.repository;

import infsus.szup.model.entity.TeamMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TeamMemberDao extends JpaRepository<TeamMemberEntity, Long> {
    Boolean existsByTeamLeaderFalse();

    @Modifying
    @Query(value = "DELETE FROM team_member where team_member_id = :teamMemberId", nativeQuery = true)
    void deleteTeamMember(Long teamMemberId);
}
