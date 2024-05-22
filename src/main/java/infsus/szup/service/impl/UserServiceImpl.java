package infsus.szup.service.impl;

import infsus.szup.mapper.UserMapper;
import infsus.szup.model.dto.users.UserResponseDTO;
import infsus.szup.repository.UserDao;
import infsus.szup.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserMapper userMapper;
    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userMapper.toUserResponseDTOs(userDao.findAll());
    }
}
