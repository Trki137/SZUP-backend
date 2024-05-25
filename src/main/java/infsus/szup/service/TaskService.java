package infsus.szup.service;

import infsus.szup.model.dto.task.*;

public interface TaskService {
    TaskResponseDTO createTask(Long projectId, Long teamId, CreateTaskRequestDTO createTaskRequestDTO);

    TaskDetailsDTO getTaskDetails(Long taskId);

    void deleteTask(Long taskId);

    TaskDetailsDTO updateStatus(UpdateTaskStatusDTO updateTaskStatusDTO);

    TaskDetailsDTO updateTask(Long taskId, UpdateTaskDTO updateTaskDTO);

    TaskDetailsDTO changeTaskSolver(Long projectId, Long taskId, TaskSolverChangeDTO taskSolverChangeDTO);

    UserTasksDTO getUserTasks(Long projectId,Long userId);
}
