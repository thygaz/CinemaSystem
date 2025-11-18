package br.com.cinema.cinemasystem.controller;

import br.com.cinema.cinemasystem.dto.movieSession.MovieSessionRequestDTO;
import br.com.cinema.cinemasystem.dto.movieSession.MovieSessionResponseDTO;
import br.com.cinema.cinemasystem.model.MovieSession;
import br.com.cinema.cinemasystem.service.MovieSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/sessions")
public class MovieSessionController {

    private final MovieSessionService movieSessionService;

    public MovieSessionController(MovieSessionService movieSessionService){
        this.movieSessionService = movieSessionService;
    }

    @PostMapping
    public ResponseEntity<MovieSessionResponseDTO> createSession(@RequestBody MovieSessionRequestDTO requestDTO){
        MovieSessionResponseDTO responseDTO = movieSessionService.createSession(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieSession> findSessionById(@PathVariable Long id){
        MovieSession movieSession = movieSessionService.findMovieSessionById(id);
        return ResponseEntity.ok(movieSession);
    }

    @GetMapping
    public List<MovieSession> findAllSessions(){
        return movieSessionService.getAllSessions();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieSession> updateSession(@PathVariable Long id, @RequestBody MovieSession sessionDetails){
        MovieSession updatedSession = movieSessionService.updateSession(id, sessionDetails);
        return ResponseEntity.ok(updatedSession);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id){
        movieSessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }

}
