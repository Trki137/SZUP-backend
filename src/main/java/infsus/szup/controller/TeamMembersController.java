package infsus.szup.controller;


import infsus.szup.model.dto.users.UserResponseDTO;
import infsus.szup.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class TeamMembersController {
    private final TeamMemberService teamMemberService;

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<UserResponseDTO>> getTeamMembers(@PathVariable Long teamId){
        return ResponseEntity.ok(teamMemberService.getTeamMembers(teamId));
    }
}
