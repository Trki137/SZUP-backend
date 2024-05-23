package infsus.szup.controller;

import infsus.szup.model.dto.task.CreateTaskRequestDTO;
import infsus.szup.model.dto.task.CreateTaskResponseDTO;
import infsus.szup.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/project/{projectId}/team/{teamId}/task/create-task")
    public ResponseEntity<CreateTaskResponseDTO> createTask(@PathVariable Long projectId, @PathVariable Long teamId, @RequestBody CreateTaskRequestDTO createTaskRequestDTO){
        return ResponseEntity.ok(taskService.createTask(projectId, teamId, createTaskRequestDTO));
    }
}
