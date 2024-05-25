package infsus.szup.dataloader;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import infsus.szup.model.entity.PriorityEntity;
import infsus.szup.repository.PriorityDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriorityDataLoader implements CommandLineRunner {
    private final ObjectMapper objectMapper;
    private final PriorityDao priorityDao;
    private static final String DATA_PATH = "/data/priority.json";
    @Override
    public void run(String... args) throws Exception {
        if (priorityDao.count() == 0) {
            log.info("Loading priorities into database from JSON: {}", DATA_PATH);
            try (InputStream inputStream = TypeReference.class.getResourceAsStream(DATA_PATH)) {
                Priorities response = objectMapper.readValue(inputStream, Priorities.class);
                priorityDao.saveAll(response.priorities());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data", e);
            }
        }
    }

    private record Priorities(List<PriorityEntity> priorities) {
    }
}
