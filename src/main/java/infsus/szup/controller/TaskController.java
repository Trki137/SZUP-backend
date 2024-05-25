package infsus.szup.controller;

import infsus.szup.model.dto.task.*;
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
    public ResponseEntity<TaskResponseDTO> createTask(@PathVariable Long projectId, @PathVariable Long teamId, @RequestBody CreateTaskRequestDTO createTaskRequestDTO) {
        return ResponseEntity.ok(taskService.createTask(projectId, teamId, createTaskRequestDTO));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<TaskDetailsDTO> getTaskDetails(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskDetails(taskId));
    }

    @GetMapping("/task/all-tasks/user/{userId}")
    public ResponseEntity<UserTasksDTO> getUserTasks(Long userId) {
        return ResponseEntity.ok(taskService.getUserTasks(userId));
    }

    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/task/update-status")
    public ResponseEntity<TaskDetailsDTO> updateStatus(@RequestBody UpdateTaskStatusDTO updateTaskStatusDTO) {
        return ResponseEntity.ok(taskService.updateStatus(updateTaskStatusDTO));
    }

    @PutMapping("/task/{taskId}")
    public ResponseEntity<TaskDetailsDTO> updateTask(@PathVariable Long taskId, @RequestBody UpdateTaskDTO updateTaskDTO) {
        return ResponseEntity.ok(taskService.updateTask(taskId, updateTaskDTO));
    }

    @PutMapping("/project/{projectId}/task/{taskId}")
    public ResponseEntity<TaskDetailsDTO> changeTaskSolver(@PathVariable Long projectId, @PathVariable Long taskId, @RequestBody TaskSolverChangeDTO taskSolverChangeDTO) {
        return ResponseEntity.ok(taskService.changeTaskSolver(projectId, taskId, taskSolverChangeDTO));
    }


}
