package infsus.szup.mapper;

import infsus.szup.model.dto.team.teammember.TeamMemberResponseDTO;
import infsus.szup.model.entity.TeamMemberEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TeamMemberMapper {
    default TeamMemberResponseDTO toTeamMemberResponse(TeamMemberEntity teamMemberEntity) {
        if (teamMemberEntity == null) {
            return null;
        } else {
            Long id = teamMemberEntity.getId();
            String firstName = teamMemberEntity.getTeamMember().getFirstName();
            String lastName = teamMemberEntity.getTeamMember().getLastName();
            Boolean isLeader = teamMemberEntity.getTeamLeader();
            return new TeamMemberResponseDTO(id, firstName, lastName, isLeader);
        }
    }
}
