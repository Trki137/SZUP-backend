package infsus.szup.mapper;

import infsus.szup.model.dto.team.TeamInfoResponseDTO;
import infsus.szup.model.dto.team.TeamResponseDTO;
import infsus.szup.model.entity.TeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {TeamMemberMapper.class})
public interface TeamMapper {
    TeamResponseDTO toTeamResponseDTO(TeamEntity team);

    TeamInfoResponseDTO toTeamInfoResponseDTO(TeamEntity team);
}
