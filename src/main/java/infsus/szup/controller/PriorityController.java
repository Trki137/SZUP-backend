package infsus.szup.controller;

import infsus.szup.model.dto.priority.PriorityResponseDTO;
import infsus.szup.service.PriorityService;
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
public class PriorityController {
    private final PriorityService priorityService;
    @GetMapping("/priority/all-priorities")
    ResponseEntity<List<PriorityResponseDTO>> getAllPriorities(){
        return ResponseEntity.ok(priorityService.getAllPriorities());
    }
}
