package infsus.szup.service.integration;


import infsus.szup.model.dto.priority.PriorityResponseDTO;
import infsus.szup.repository.PriorityDao;
import infsus.szup.service.PriorityService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PriorityApiIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PriorityDao priorityDao;

    @Autowired
    private PriorityService priorityService;

    private static HttpHeaders headers;

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }
    private String createURLWithPort() {
        return String.format("http://localhost:%d/api/priority/all-priorities", port);
    }

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }
    @Test
    public void testGetAllStatuses() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<List<PriorityResponseDTO>> response = testRestTemplate.exchange(
                createURLWithPort(), HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
                }
        );

        List<PriorityResponseDTO> priorities = response.getBody();
        assert priorities != null;
        assertEquals(response.getStatusCode().value(), 200);
        assertEquals(priorities.size(), 4);
        assertEquals(priorities.size(), priorityService.getAllPriorities().size());
        assertEquals(priorities.size(), priorityDao.findAll().size());
    }
}
