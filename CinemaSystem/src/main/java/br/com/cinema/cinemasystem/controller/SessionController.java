package br.com.cinema.cinemasystem.controller;

import br.com.cinema.cinemasystem.dto.SessionRequestDTO;
import br.com.cinema.cinemasystem.dto.SessionResponseDTO;
import br.com.cinema.cinemasystem.model.Session;
import br.com.cinema.cinemasystem.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public SessionResponseDTO save(@RequestBody SessionRequestDTO sessionRequestDTO) {
        return sessionService.create(sessionRequestDTO);
    }

    @GetMapping(name = "/sessions/")
    public List<SessionResponseDTO> listAll() {
        return sessionService.findAll();
    }

    @GetMapping(name = "/sessions/{uuid}")
    public ResponseEntity<SessionResponseDTO> findById(@PathVariable UUID uuid) {
        return sessionService.findById(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(name = "/sessions/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        sessionService.delete(uuid);

        return ResponseEntity.noContent().build();
    }
}
