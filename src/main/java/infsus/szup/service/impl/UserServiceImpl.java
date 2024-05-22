package infsus.szup.service.impl;

import infsus.szup.model.dto.users.UserResponseDTO;
import infsus.szup.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<UserResponseDTO> getAllUsers() {
        return null;
    }
}
