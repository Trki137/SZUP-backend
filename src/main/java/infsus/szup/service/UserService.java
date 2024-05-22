package infsus.szup.service;

import infsus.szup.model.dto.users.UserResponseDTO;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
}
