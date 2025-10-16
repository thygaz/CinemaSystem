package br.com.cinema.cinemasystem.controller;

import br.com.cinema.cinemasystem.model.Film;
import br.com.cinema.cinemasystem.service.FilmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film save(@RequestBody Film film) {
        return filmService.save(film);
    }

    @GetMapping(name = "/films/")
    public List<Film> listAll() {
        return filmService.findAll();
    }

    @GetMapping(name = "/films/{uuid}")
    public ResponseEntity<?> findById(@PathVariable UUID uuid) {
        Optional<Film> film = filmService.findById(uuid);

        return ResponseEntity.ok(film);
    }

    @DeleteMapping(name = "/films/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        filmService.delete(uuid);

        return ResponseEntity.noContent().build();
    }
}
