package infsus.szup.mapper;

import infsus.szup.model.dto.priority.PriorityResponseDTO;
import infsus.szup.model.entity.PriorityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PriorityMapper {
    PriorityResponseDTO toPriorityResponseDTO(PriorityEntity priority);
}
