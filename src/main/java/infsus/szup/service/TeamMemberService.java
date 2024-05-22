package infsus.szup.service;

import infsus.szup.model.dto.team.teammember.TeamMemberRequestDTO;

public interface TeamMemberService {
    void createTeamMember(TeamMemberRequestDTO teamMemberRequestDTO, Long teamId);
}
