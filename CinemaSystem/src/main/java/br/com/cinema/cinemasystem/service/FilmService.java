package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.dto.FilmRequestDTO;
import br.com.cinema.cinemasystem.dto.FilmResponseDTO;
import br.com.cinema.cinemasystem.model.Film;
import br.com.cinema.cinemasystem.repository.FilmRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public FilmResponseDTO create(FilmRequestDTO filmRequestDTO) {
        Film film = new Film(filmRequestDTO.name());

        Film savedFilm = filmRepository.save(film);

        return new FilmResponseDTO(savedFilm.getUuid(), savedFilm.getName());
    }

    public List<FilmResponseDTO> findAll() {
        return filmRepository.findAll()
                .stream()
                .map(film -> new FilmResponseDTO(film.getUuid(), film.getName()))
                .toList();
    }

    public Optional<FilmResponseDTO> findById(UUID uuid) {
        return filmRepository.findById(uuid)
                .map(film -> new FilmResponseDTO(film.getUuid(), film.getName()));
    }

    public void delete(UUID uuid) {
        if (filmRepository.existsById(uuid)) {
            filmRepository.deleteById(uuid);
        } else {
            throw new RuntimeException("Filme não encontrado.");
        }
    }
}
