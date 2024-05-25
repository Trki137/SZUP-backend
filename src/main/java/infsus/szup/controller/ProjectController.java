package infsus.szup.controller;

import infsus.szup.model.dto.project.ProjectDetailsResponseDTO;
import infsus.szup.model.dto.project.ProjectRequestDTO;
import infsus.szup.model.dto.project.ProjectResponseDTO;
import infsus.szup.model.dto.project.UpdateProjectReqDTO;
import infsus.szup.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/project/all-projects/user/{userId}")
    ResponseEntity<List<ProjectResponseDTO>> getAllProjectsForUser(@PathVariable Long userId){
        return ResponseEntity.ok(projectService.getAllProjectsForUser(userId));
    }

    @GetMapping("/project/{projectId}/user/{userId}")
    ResponseEntity<ProjectDetailsResponseDTO> getProjectDetails(@PathVariable Long projectId, @PathVariable Long userId){
        return ResponseEntity.ok(projectService.getProjectDetails(projectId, userId));
    }

    @PostMapping("/project/create")
    ResponseEntity<ProjectResponseDTO> createProject(@RequestBody ProjectRequestDTO projectRequestDTO) {
        return ResponseEntity.ok(projectService.createProject(projectRequestDTO));
    }

    @PutMapping("/project/{projectId}")
    ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long projectId, @RequestBody UpdateProjectReqDTO updateProjectReqDTO) {
        return ResponseEntity.ok(projectService.updateProject(projectId, updateProjectReqDTO));
    }

    @DeleteMapping("/project/{projectId}")
    ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok().build();
    }
}
