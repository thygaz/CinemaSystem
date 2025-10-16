package br.com.cinema.cinemasystem.controller;

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
    public Session save(@RequestBody Session session) {
        return sessionService.save(session);
    }

    @GetMapping(name = "/sessions/")
    public List<Session> listAll() {
        return sessionService.findAll();
    }

    @GetMapping(name = "/sessions/{uuid}")
    public ResponseEntity<?> findById(@PathVariable UUID uuid) {
        Optional<Session> session = sessionService.findById(uuid);

        return ResponseEntity.ok(session);
    }

    @DeleteMapping(name = "/sessions/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        sessionService.delete(uuid);

        return ResponseEntity.noContent().build();
    }
}
