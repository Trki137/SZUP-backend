package infsus.szup.dataloader;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import infsus.szup.model.entity.StatusEntity;
import infsus.szup.repository.StatusDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("test")
public class StatusDataLoader implements CommandLineRunner {
    private final ObjectMapper objectMapper;
    private final StatusDao statusDao;
    private static final String DATA_PATH = "/data/status.json";

    @Override
    public void run(String... args) {
        if (statusDao.count() == 0) {
            log.info("Loading statutes into database from JSON: {}", DATA_PATH);
            try (InputStream inputStream = TypeReference.class.getResourceAsStream(DATA_PATH)) {
                Statuses response = objectMapper.readValue(inputStream, Statuses.class);
                statusDao.saveAll(response.statuses());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data", e);
            }
        }
    }

    private record Statuses(List<StatusEntity> statuses) {
    }
}
