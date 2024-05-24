package infsus.szup.service.integration;

import infsus.szup.model.dto.status.StatusResponseDTO;
import infsus.szup.repository.StatusDao;
import infsus.szup.service.StatusService;
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
public class StatusApiIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StatusDao statusDao;

    @Autowired
    private StatusService statusService;

    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    private String createURLWithPort() {
        return String.format("http://localhost:%d/api/status/all-statuses", port);
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
        assertEquals(statuses.size(), statusService.getAllStatuses().size());
        assertEquals(statuses.size(), statusDao.findAll().size());
    }
}
