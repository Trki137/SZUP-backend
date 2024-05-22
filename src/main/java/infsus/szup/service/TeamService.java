package infsus.szup.service;

import infsus.szup.model.dto.team.TeamRequestDTO;
import infsus.szup.model.dto.team.TeamUpdateRequestDTO;

public interface TeamService {
    void createTeam(TeamRequestDTO teamRequestDTO, Long projectId);

    void deleteTeam(Long projectId, Long teamId);

    void updateTeam(TeamUpdateRequestDTO teamUpdateRequestDTO, Long projectId, Long teamId);

    void addMember(Long projectId, Long teamId, Long memberId, Long addedByUserId);

    void removeMember(Long projectId, Long teamId, Long memberId, Long removedByUserId);
}
