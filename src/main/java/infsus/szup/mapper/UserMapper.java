package infsus.szup.mapper;

import infsus.szup.model.dto.users.UserResponseDTO;
import infsus.szup.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toUserResponseDTO(UserEntity userEntity);
}
