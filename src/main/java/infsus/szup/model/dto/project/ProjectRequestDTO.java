package infsus.szup.model.dto.project;

import infsus.szup.model.dto.team.TeamRequestDTO;

import java.util.List;

public record ProjectRequestDTO(String projectName, Long createdById,List<TeamRequestDTO> teams) {

}
