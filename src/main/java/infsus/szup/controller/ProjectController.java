package infsus.szup.controller;

import infsus.szup.model.dto.project.ProjectRequestDTO;
import infsus.szup.model.dto.project.ProjectResponseDTO;
import infsus.szup.model.dto.project.UpdateProjectReqDTO;
import infsus.szup.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {
    private final ProjectService projectService;
    @PostMapping("/project/create")
    ResponseEntity<ProjectResponseDTO> createTeam(@RequestBody ProjectRequestDTO projectRequestDTO){
        return ResponseEntity.ok(projectService.createProject(projectRequestDTO));
    }

    @PutMapping("/project/{projectId}")
    ResponseEntity<ProjectResponseDTO> createTeam(@PathVariable Long projectId, @RequestBody UpdateProjectReqDTO updateProjectReqDTO){
        return ResponseEntity.ok(projectService.updateProject(projectId, updateProjectReqDTO));
    }

    @PutMapping("/project/{projectId}")
    ResponseEntity<Void> createTeam(@PathVariable Long projectId){
        projectService.deleteProject(projectId);
        return ResponseEntity.ok().build();
    }
}
