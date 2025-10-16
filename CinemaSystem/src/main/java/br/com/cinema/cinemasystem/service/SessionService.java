package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.model.Film;
import br.com.cinema.cinemasystem.model.Session;
import br.com.cinema.cinemasystem.repository.FilmRepository;
import br.com.cinema.cinemasystem.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {

    private final FilmRepository filmRepository;
    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository, FilmRepository filmRepository) {
        this.sessionRepository = sessionRepository;
        this.filmRepository = filmRepository;
    }

    public Session save(Session session) {
        // precisa validar se o filme existe
        Film film = filmRepository.findById(session.getFilm().getUuid())
                .orElseThrow(() -> new RuntimeException("Não foi possível criar a sessão, filme não encontrado."));

        return sessionRepository.save(session);
    }

    public List<Session> findAll() {
        return sessionRepository.findAll();
    }

    public Optional<Session> findById(UUID uuid) {
        return sessionRepository.findById(uuid);
    }

    // criar métodU que lista as sessões de um filme específico
    public List<Session> listByFilm(UUID uuid) {
        Film film = filmRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("Esse filme não está em nenhuma sessão."));

        return sessionRepository.findByFilm(film);
    }
}
