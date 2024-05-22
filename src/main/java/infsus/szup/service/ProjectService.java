package infsus.szup.service;

import infsus.szup.model.dto.project.ProjectRequestDTO;
import infsus.szup.model.dto.project.ProjectResponseDTO;
import infsus.szup.model.dto.project.UpdateProjectReqDTO;

public interface ProjectService {
    ProjectResponseDTO createProject(ProjectRequestDTO projectRequestDTO);
    ProjectResponseDTO updateProject(Long projectId,UpdateProjectReqDTO updateProjectReqDTO);
    void deleteProject(Long projectId);
}
