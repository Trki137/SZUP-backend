package infsus.szup.service.integration;


import infsus.szup.model.dto.status.StatusResponseDTO;
import infsus.szup.repository.PriorityDao;
import infsus.szup.service.PriorityService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }
    private String createURLWithPort() {
        return String.format("http://localhost:%d/api/priority/all-priorities", port);
    }

    @Test
    public void testGetAllStatuses() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<List<StatusResponseDTO>> response = testRestTemplate.exchange(
                createURLWithPort(), HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
                }
        );

        List<StatusResponseDTO> statuses = response.getBody();
        assert statuses != null;
        assertEquals(response.getStatusCode().value(), 200);
        assertEquals(statuses.size(), priorityService.getAllPriorities().size());
        assertEquals(statuses.size(), priorityDao.findAll().size());
    }
}
