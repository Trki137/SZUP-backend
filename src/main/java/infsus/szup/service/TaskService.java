package infsus.szup.service;

import infsus.szup.model.dto.task.CreateTaskRequestDTO;
import infsus.szup.model.dto.task.CreateTaskResponseDTO;

public interface TaskService {
    CreateTaskResponseDTO createTask(Long projectId, Long teamId, CreateTaskRequestDTO createTaskRequestDTO);
}
