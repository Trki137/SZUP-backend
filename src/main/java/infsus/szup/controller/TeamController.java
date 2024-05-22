package infsus.szup.controller;

import infsus.szup.model.dto.team.TeamRequestDTO;
import infsus.szup.model.dto.team.TeamResponseDTO;
import infsus.szup.model.dto.team.TeamUpdateRequestDTO;
import infsus.szup.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class TeamController {
    private final TeamService teamService;

    @DeleteMapping("/project/{projectId}/team/{teamId}")
    ResponseEntity<Void> deleteTeam(@PathVariable Long projectId, @PathVariable Long teamId) {
        teamService.deleteTeam(projectId, teamId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/project/{projectId}/team")
    ResponseEntity<TeamResponseDTO> deleteTeam(@RequestBody TeamRequestDTO teamRequestDTO, @PathVariable Long projectId) {
        return ResponseEntity.ok(teamService.createTeam(teamRequestDTO, projectId));
    }

    @PutMapping("/project/{projectId}/team/{teamId}/add-member/{memberId}/{addedById}")
    ResponseEntity<TeamResponseDTO> addMember(@PathVariable Long projectId, @PathVariable Long teamId, @PathVariable Long memberId, @PathVariable Long addedById) {
        return ResponseEntity.ok(teamService.addMember(projectId, teamId, memberId, addedById));
    }

    @PutMapping("/project/{projectId}/team/{teamId}/remove-member/{memberId}/{removedBy}")
    ResponseEntity<TeamResponseDTO> removeMember(@PathVariable Long projectId, @PathVariable Long teamId, @PathVariable Long memberId, @PathVariable Long removedBy) {
        return ResponseEntity.ok(teamService.removeMember(projectId, teamId, memberId, removedBy));
    }

    @PutMapping("/project/{projectId}/team/{teamId}")
    ResponseEntity<TeamResponseDTO> updateTeam(@RequestBody TeamUpdateRequestDTO teamUpdateRequestDTO, @PathVariable Long projectId, @PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.updateTeam(teamUpdateRequestDTO, projectId, teamId));
    }


}
