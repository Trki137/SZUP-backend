package infsus.szup.dataloader;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import infsus.szup.model.dto.task.CreateTaskRequestDTO;
import infsus.szup.repository.TaskDao;
import infsus.szup.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(6)
@Profile("test")
public class TaskDataLoader implements CommandLineRunner {
    private final ObjectMapper objectMapper;
    private final TaskDao taskDao;
    private final TaskService taskService;
    private static final String DATA_PATH = "/data/task.json";

    @Override
    public void run(String... args) {
        if (taskDao.count() == 0) {
            log.info("Loading tasks into database from JSON: {}", DATA_PATH);
            try (InputStream inputStream = TypeReference.class.getResourceAsStream(DATA_PATH)) {
                Tasks response = objectMapper.readValue(inputStream, Tasks.class);
                response.tasks.forEach(task -> taskService.createTask(task.projectId, task.teamId, task.createTaskRequestDTO));
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data", e);
            }
        }
    }

    private record Task(Long projectId, Long teamId, CreateTaskRequestDTO createTaskRequestDTO) {
    }

    private record Tasks(List<Task> tasks) {
    }
}
