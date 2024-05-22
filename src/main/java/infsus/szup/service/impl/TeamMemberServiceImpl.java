package infsus.szup.service.impl;

import infsus.szup.mapper.UserMapper;
import infsus.szup.model.dto.team.teammember.TeamMemberRequestDTO;
import infsus.szup.model.dto.users.UserResponseDTO;
import infsus.szup.model.entity.TeamEntity;
import infsus.szup.model.entity.TeamMemberEntity;
import infsus.szup.model.entity.UserEntity;
import infsus.szup.repository.TeamDao;
import infsus.szup.repository.TeamMemberDao;
import infsus.szup.repository.UserDao;
import infsus.szup.service.TeamMemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService {
    private final TeamDao teamDao;
    private final UserDao userDao;
    private final TeamMemberDao teamMemberDao;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public void createTeamMember(TeamMemberRequestDTO teamMemberRequestDTO, Long teamId) {
        final TeamEntity team = teamDao.findById(teamId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Team with id %d not found!", teamId))
        );

        final UserEntity user = userDao.findById(teamMemberRequestDTO.userId()).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id %d not found!", teamMemberRequestDTO.userId()))
        );

        final TeamMemberEntity teamMemberEntity = new TeamMemberEntity();
        teamMemberEntity.setTeamLeader(teamMemberRequestDTO.isLeader());
        teamMemberEntity.setTeam(team);
        teamMemberEntity.setTeamMember(user);
        teamMemberDao.save(teamMemberEntity);
    }

    @Override
    public List<UserResponseDTO> getTeamMembers(Long teamId) {
        return teamDao.findById(teamId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Team with id %d not found", teamId))
        ).getTeamMembers().stream().map(TeamMemberEntity::getTeamMember).map(userMapper::toUserResponseDTO).toList();
    }
}
