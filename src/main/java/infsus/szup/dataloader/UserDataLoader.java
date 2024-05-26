package infsus.szup.dataloader;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import infsus.szup.model.entity.UserEntity;
import infsus.szup.repository.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
public class UserDataLoader implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final UserDao userDao;
    private static final String DATA_PATH = "/data/users.json";

    @Override
    public void run(String... args) {
        if (userDao.count() == 0) {
            log.info("Loading users into database from JSON: {}", DATA_PATH);
            try (InputStream inputStream = TypeReference.class.getResourceAsStream(DATA_PATH)) {
                Users response = objectMapper.readValue(inputStream, Users.class);
                userDao.saveAll(response.users);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data", e);
            }
        }
    }

    private record Users(List<UserEntity> users) {
    }
}

