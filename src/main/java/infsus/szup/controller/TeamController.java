package infsus.szup.controller;

import infsus.szup.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class TeamController {
    private TeamService teamService;

    @DeleteMapping("/project/{projectId}/team/{teamId}")
    ResponseEntity<Void> deleteTeam(@PathVariable Long projectId,@PathVariable Long teamId){
        teamService.deleteTeam(projectId, teamId);
        return ResponseEntity.ok().build();
    }


}
