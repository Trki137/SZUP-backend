package infsus.szup.model.dto.team;

import infsus.szup.model.dto.team.teammember.TeamMemberResponseDTO;

import java.util.List;

public record TeamResponseDTO(Long id, String teamName, List<TeamMemberResponseDTO> teamMembers) {
}
