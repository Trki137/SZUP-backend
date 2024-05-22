package infsus.szup.model.dto.team;

import infsus.szup.model.dto.team.teammember.TeamMemberRequestDTO;

import java.util.List;

public record TeamRequestDTO(String teamName, List<TeamMemberRequestDTO> teamMembers) {
}
