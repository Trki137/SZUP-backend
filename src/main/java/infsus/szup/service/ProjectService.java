package infsus.szup.service;

import infsus.szup.model.dto.project.ProjectRequestDTO;
import infsus.szup.model.dto.project.ProjectResponseDTO;

public interface ProjectService {
    ProjectResponseDTO createProject(ProjectRequestDTO projectRequestDTO);
}
