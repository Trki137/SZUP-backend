package infsus.szup.mapper;

import infsus.szup.model.dto.users.UserResponseDTO;
import infsus.szup.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    List<UserResponseDTO> toUserResponseDTOs(List<UserEntity> entities);
    UserResponseDTO toUserResponseDTO(UserEntity entity);
}
