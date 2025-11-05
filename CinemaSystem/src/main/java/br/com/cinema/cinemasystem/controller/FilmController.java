package br.com.cinema.cinemasystem.controller;

import br.com.cinema.cinemasystem.dto.FilmRequestDTO;
import br.com.cinema.cinemasystem.dto.FilmResponseDTO;
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
    public FilmResponseDTO save(@RequestBody FilmRequestDTO filmRequestDTO) {
        return filmService.create(filmRequestDTO);
    }

    @GetMapping(name = "/films/")
    public List<FilmResponseDTO> listAll() {
        return filmService.findAll();
    }

    @GetMapping(name = "/films/{uuid}")
    public ResponseEntity<FilmResponseDTO> findById(@PathVariable UUID uuid) {
        return filmService.findById(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(name = "/films/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        filmService.delete(uuid);

        return ResponseEntity.noContent().build();
    }
}
