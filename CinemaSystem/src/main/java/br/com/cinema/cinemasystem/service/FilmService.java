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

    public Film save(Film film) {
        return filmRepository.save(film);
    }

    public FilmResponseDTO create(FilmRequestDTO filmRequestDTO) {
        // 1. Converte DTO para Entidade (direto aqui)
        Film film = new Film(null, filmRequestDTO.name());

        // 2. Salva no banco de dados
        Film savedFilm = filmRepository.save(film);

        // 3. Converte Entidade para DTO de resposta (direto aqui)
        return new FilmResponseDTO(savedFilm.getUuid(), savedFilm.getName());
    }

    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    public Optional<Film> findById(UUID uuid) {
        return filmRepository.findById(uuid);
    }

    public void delete(UUID uuid) {
        if (filmRepository.existsById(uuid)) {
            filmRepository.deleteById(uuid);
        } else {
            throw new RuntimeException("Filme não encontrado.");
        }
    }
}
