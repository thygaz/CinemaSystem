package br.com.cinema.cinemasystem.controller;

import br.com.cinema.cinemasystem.model.Film;
import br.com.cinema.cinemasystem.model.Ticket;
import br.com.cinema.cinemasystem.service.FilmService;
import br.com.cinema.cinemasystem.service.TicketService;
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

    @PostMapping(name = )
    public save(@RequestBody film) {

    }

    @GetMapping(name = "")
    public List<Film> listAll() {

    }

    @GetMapping(name = "")
    public List<Film> findById(@PathVariable UUID uuid) {
        Optional<Film> film = filmService.findById(uuid);
    }

    @DeleteMapping(name = "/films/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {

    }
}
