package infsus.szup.service;

import infsus.szup.model.dto.team.TeamRequestDTO;

public interface TeamService {
    void createTeam(TeamRequestDTO teamRequestDTO, Long projectId);
}
