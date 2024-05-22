package infsus.szup.mapper;

import infsus.szup.model.dto.team.teammember.TeamMemberResponseDTO;
import infsus.szup.model.entity.TeamMemberEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TeamMemberMapper {
    TeamMemberResponseDTO toTeamMemberResponse(TeamMemberEntity teamMemberEntity);
}
