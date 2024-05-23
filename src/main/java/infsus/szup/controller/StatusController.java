package infsus.szup.controller;

import infsus.szup.model.dto.status.StatusResponseDTO;
import infsus.szup.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin(origins = "http://localhost:5173")
public class StatusController {
    private final StatusService statusService;

    @GetMapping("/status/all-statuses")
    ResponseEntity<List<StatusResponseDTO>> getAllStatues() {
        return ResponseEntity.ok(statusService.getAllStatuses());
    }
}
