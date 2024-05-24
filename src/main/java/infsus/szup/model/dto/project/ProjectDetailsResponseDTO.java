package infsus.szup.model.dto.project;

import infsus.szup.model.dto.rights.UserRightsResponseDTO;
import infsus.szup.model.dto.team.TeamResponseDTO;

import java.util.List;

public record ProjectDetailsResponseDTO(Long id, String projectName, UserRightsResponseDTO userRightsResponseDTO, List<TeamResponseDTO> teams, TeamResponseDTO teamLeaderOf) {
}
