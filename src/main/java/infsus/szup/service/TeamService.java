package infsus.szup.service;

import infsus.szup.model.dto.team.TeamInfoResponseDTO;
import infsus.szup.model.dto.team.TeamRequestDTO;
import infsus.szup.model.dto.team.TeamResponseDTO;
import infsus.szup.model.dto.team.TeamUpdateRequestDTO;

import java.util.List;

public interface TeamService {
    TeamResponseDTO createTeam(TeamRequestDTO teamRequestDTO, Long projectId);

    void deleteTeam(Long projectId, Long teamId);

    TeamResponseDTO updateTeam(TeamUpdateRequestDTO teamUpdateRequestDTO, Long projectId, Long teamId);

    TeamResponseDTO addMember(Long projectId, Long teamId, Long memberId, Long addedByUserId);

    TeamResponseDTO removeMember(Long projectId, Long teamId, Long memberId, Long removedByUserId);

    List<TeamInfoResponseDTO> getTeamsForProject(Long projectId);
}
