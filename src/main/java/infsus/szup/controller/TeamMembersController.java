package infsus.szup.controller;


import infsus.szup.model.dto.users.UserResponseDTO;
import infsus.szup.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:5173")
public class TeamMembersController {
    private final TeamMemberService teamMemberService;

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<UserResponseDTO>> getTeamMembers(@PathVariable Long teamId){
        return ResponseEntity.ok(teamMemberService.getTeamMembers(teamId));
    }
}
