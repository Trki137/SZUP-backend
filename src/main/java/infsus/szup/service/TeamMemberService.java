package infsus.szup.service;

import infsus.szup.model.dto.team.teammember.TeamMemberRequestDTO;
import infsus.szup.model.dto.users.UserResponseDTO;

import java.util.List;

public interface TeamMemberService {
    void createTeamMember(TeamMemberRequestDTO teamMemberRequestDTO, Long teamId);

    List<UserResponseDTO> getTeamMembers(Long teamId);
}
