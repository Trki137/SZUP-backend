package infsus.szup.service.unit.service;

import infsus.szup.mapper.UserMapper;
import infsus.szup.model.dto.users.UserResponseDTO;
import infsus.szup.model.entity.UserEntity;
import infsus.szup.repository.UserDao;
import infsus.szup.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers_Success() {
        UserResponseDTO user1 = new UserResponseDTO(1L, "Dean", "Trkulja");
        UserResponseDTO user2 = new UserResponseDTO(2L, "Josipa", "Mihaljević");

        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(new UserEntity(1L, "Dean", "Trkulja", "dt@gmail.com", "pass"));
        userEntities.add(new UserEntity(1L, "Josipa", "Mihaljević", "jm@gmail.com", "pass"));

        List<UserResponseDTO> responseDTOS = new ArrayList<>();
        responseDTOS.add(user1);
        responseDTOS.add(user2);


        when(userDao.findAll()).thenReturn(userEntities);
        when(userMapper.toUserResponseDTOs(userEntities)).thenReturn(responseDTOS);

        List<UserResponseDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("Dean", result.get(0).firstName());
        assertEquals("Josipa", result.get(1).firstName());

        verify(userDao, times(1)).findAll();
        verify(userMapper, times(1)).toUserResponseDTOs(userEntities);
        verifyNoMoreInteractions(userDao);
        verifyNoMoreInteractions(userMapper);
    }

    @Test
    void testGetAllUsers_EmptyList() {
        when(userDao.findAll()).thenReturn(Collections.emptyList());

        List<UserResponseDTO> result = userService.getAllUsers();

        assertEquals(0, result.size());

        verify(userDao, times(1)).findAll();
        verifyNoMoreInteractions(userDao);
    }
}

