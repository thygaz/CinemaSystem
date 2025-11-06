package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.dto.FilmResponseDTO;
import br.com.cinema.cinemasystem.dto.SessionRequestDTO;
import br.com.cinema.cinemasystem.dto.SessionResponseDTO;
import br.com.cinema.cinemasystem.model.Film;
import br.com.cinema.cinemasystem.model.Session;
import br.com.cinema.cinemasystem.repository.FilmRepository;
import br.com.cinema.cinemasystem.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public SessionResponseDTO create(SessionRequestDTO sessionRequestDTO) {
        Film film = filmRepository.findById(sessionRequestDTO.filmUuid())
                .orElseThrow(() -> new RuntimeException("Filme não encontrado."));

        Session session = new Session(film, sessionRequestDTO.dateTime(), sessionRequestDTO.price());

        Session savedSession = sessionRepository.save(session);

        FilmResponseDTO filmResponseDTO = new FilmResponseDTO(film.getUuid(), film.getName());

        return new SessionResponseDTO(savedSession.getUuid(),
                filmResponseDTO,
                savedSession.getDateTime(),
                savedSession.getPrice());
    }

    @Transactional(readOnly = true)
    public List<SessionResponseDTO> findAll() {
        return sessionRepository.findAll()
                .stream()
                .map(session -> new SessionResponseDTO(session.getUuid(),
                        new FilmResponseDTO(
                                session.getFilm().getUuid(),
                                session.getFilm().getName()
                        ),
                        session.getDateTime(),
                        session.getPrice()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<SessionResponseDTO> findById(UUID uuid) {
        return sessionRepository.findById(uuid)
                .map(session -> new SessionResponseDTO(session.getUuid(),
                        new FilmResponseDTO(
                                session.getFilm().getUuid(),
                                session.getFilm().getName()
                        ),
                        session.getDateTime(),
                        session.getPrice()));
    }

    @Transactional(readOnly = true)
    public List<SessionResponseDTO> findByFilmId(UUID filmUuid) {
        Film film = filmRepository.findById(filmUuid)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado."));

        return sessionRepository.findByFilm(film)
                .stream()
                .map(session -> new SessionResponseDTO(session.getUuid(),
                        new FilmResponseDTO(
                                session.getFilm().getUuid(),
                                session.getFilm().getName()
                        ),
                        session.getDateTime(),
                        session.getPrice()))
                .toList();
    }

    @Transactional
    public void delete(UUID uuid) {
        if (sessionRepository.existsById(uuid)) {
            sessionRepository.deleteById(uuid);
        } else {
            throw new RuntimeException("Sessão não encontrada.");
        }
    }
}
