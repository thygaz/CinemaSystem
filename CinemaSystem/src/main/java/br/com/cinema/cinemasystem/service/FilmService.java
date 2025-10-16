package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.model.Film;
import br.com.cinema.cinemasystem.repository.FilmRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FilmService {

    private FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public Film save(Film film) {
        return filmRepository.save(film);
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
            throw new RuntimeException("Filme não encontrado");
        }
    }
}
