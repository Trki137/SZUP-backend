package infsus.szup.mapper;

import infsus.szup.model.dto.status.StatusResponseDTO;
import infsus.szup.model.entity.StatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface StatusMapper {
    StatusResponseDTO toStatusResponseDTO(StatusEntity status);
}
