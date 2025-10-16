package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.model.Session;
import br.com.cinema.cinemasystem.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {

    private SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session save(Session session) {
        // precisa validar se o filme existe
        return sessionRepository.save(session);
    }

    public List<Session> findAll() {
        return sessionRepository.findAll();
    }

    public Optional<Session> findById(UUID uuid) {
        return sessionRepository.findById(uuid);
    }

    // criar métodU que lista as sessões de um filme específico
}
