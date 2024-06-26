package infsus.szup.controller;

import infsus.szup.model.dto.team.TeamInfoResponseDTO;
import infsus.szup.model.dto.team.TeamRequestDTO;
import infsus.szup.model.dto.team.TeamResponseDTO;
import infsus.szup.model.dto.team.TeamUpdateRequestDTO;
import infsus.szup.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:5173")
public class TeamController {
    private final TeamService teamService;

    @DeleteMapping("/project/{projectId}/team/{teamId}")
    ResponseEntity<Void> deleteTeam(@PathVariable Long projectId, @PathVariable Long teamId) {
        teamService.deleteTeam(projectId, teamId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/project/{projectId}/team")
    ResponseEntity<TeamResponseDTO> createTeam(@RequestBody TeamRequestDTO teamRequestDTO, @PathVariable Long projectId) {
        return ResponseEntity.ok(teamService.createTeam(teamRequestDTO, projectId));
    }

    @PostMapping("/project/{projectId}/team/{teamId}/add-member/{memberId}/{addedById}")
    ResponseEntity<TeamResponseDTO> addMember(@PathVariable Long projectId, @PathVariable Long teamId, @PathVariable Long memberId, @PathVariable Long addedById) {
        return ResponseEntity.ok(teamService.addMember(projectId, teamId, memberId, addedById));
    }

    @DeleteMapping("/project/{projectId}/team/{teamId}/remove-member/{memberId}/{removedBy}")
    ResponseEntity<TeamResponseDTO> removeMember(@PathVariable Long projectId, @PathVariable Long teamId, @PathVariable Long memberId, @PathVariable Long removedBy) {
        teamService.removeMember(projectId, teamId, memberId, removedBy);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/project/{projectId}/team/{teamId}")
    ResponseEntity<TeamResponseDTO> updateTeam(@RequestBody TeamUpdateRequestDTO teamUpdateRequestDTO, @PathVariable Long projectId, @PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.updateTeam(teamUpdateRequestDTO, projectId, teamId));
    }

    @GetMapping("/project/{projectId}/teams")
    public ResponseEntity<List<TeamInfoResponseDTO>> getTeamMembers(@PathVariable Long projectId) {
        return ResponseEntity.ok(teamService.getTeamsForProject(projectId));
    }


}
