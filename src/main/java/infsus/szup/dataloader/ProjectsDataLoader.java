package infsus.szup.dataloader;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import infsus.szup.model.dto.project.ProjectRequestDTO;
import infsus.szup.repository.ProjectDao;
import infsus.szup.service.ProjectService;
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
@Order(4)
public class ProjectsDataLoader implements CommandLineRunner {
    private final ObjectMapper objectMapper;
    private final ProjectDao projectDao;
    private final ProjectService projectService;
    private static final String DATA_PATH = "/data/projects.json";

    @Override
    public void run(String... args) {
        if (projectDao.count() == 0) {
            log.info("Loading projects into database from JSON: {}", DATA_PATH);
            try (InputStream inputStream = TypeReference.class.getResourceAsStream(DATA_PATH)) {
                Projects response = objectMapper.readValue(inputStream, Projects.class);
                response.projectRequestDTOS.forEach(projectService::createProject);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data", e);
            }
        }
    }

    private record Projects(List<ProjectRequestDTO> projectRequestDTOS) {
    }
}
