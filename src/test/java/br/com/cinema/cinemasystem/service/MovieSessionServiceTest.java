package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.dto.MovieSessionRequestDTO;
import br.com.cinema.cinemasystem.dto.MovieSessionResponseDTO;
import br.com.cinema.cinemasystem.exception.ResourceNotFoundException;
import br.com.cinema.cinemasystem.model.Movie;
import br.com.cinema.cinemasystem.model.MovieSession;
import br.com.cinema.cinemasystem.model.Theater;
import br.com.cinema.cinemasystem.repository.MovieRepository;
import br.com.cinema.cinemasystem.repository.MovieSessionRepository;
import br.com.cinema.cinemasystem.repository.TheaterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieSessionServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private TheaterRepository theaterRepository;

    @Mock
    private MovieSessionRepository movieSessionRepository;

    @InjectMocks
    private MovieSessionService movieSessionService;

    private Movie movie;
    private Theater theater;
    private MovieSessionRequestDTO requestDTO;
    private MovieSession movieSession;

    @BeforeEach
    void setup(){
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Filme Teste");
        movie.setGenre("Ação");
        movie.setDurationInMinutes(120);

        theater = new Theater();
        theater.setId(1L);
        theater.setName("Sala 1");

        requestDTO = new MovieSessionRequestDTO();
        requestDTO.setMovieId(1L);
        requestDTO.setTheaterId(1L);
        requestDTO.setSessionTime(LocalDateTime.now().plusHours(2));
        requestDTO.setTicketPrice(30.0);

        movieSession = new MovieSession();
        movieSession.setId(1L);
        movieSession.setMovie(movie);
        movieSession.setTheater(theater);
        movieSession.setSessionTime(requestDTO.getSessionTime());
        movieSession.setTicketPrice(requestDTO.getTicketPrice());
    }

    @Test
    void testCreateMovieSession_Success() {

        when(movieRepository.findById(requestDTO.getMovieId())).thenReturn(Optional.of(movie));

        when(theaterRepository.findById(requestDTO.getTheaterId())).thenReturn(Optional.of(theater));

        when(movieSessionRepository.save(any(MovieSession.class))).thenReturn(movieSession);

        MovieSessionResponseDTO result = movieSessionService.createSession(requestDTO);

        assertNotNull(result);
        assertEquals(movieSession.getId(), result.getId());
        assertEquals(movie.getTitle(), result.getMovieTitle());
        assertEquals(movie.getGenre(), result.getMovieGenre());
        assertEquals(theater.getName(), result.getTheaterName());
        assertEquals(requestDTO.getTicketPrice(), result.getTicketPrice());

        verify(movieSessionRepository, times (1)).save(any(MovieSession.class));
    }


    @Test
    void testCreateMovieSession_Fail_MovieNotFound() {

        when(movieRepository.findById(requestDTO.getMovieId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> {
                    movieSessionService.createSession(requestDTO);
                }
        );

        assertEquals("Filme não encontrado com o id: " + requestDTO.getMovieId(), exception.getMessage());
        verify(movieSessionRepository, never()).save(any(MovieSession.class));
    }

    @Test
    void testCreateMovieSession_Fail_SalaNotFound() {

        when(movieRepository.findById(requestDTO.getMovieId())).thenReturn(Optional.of(movie));
        when(theaterRepository.findById(requestDTO.getTheaterId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> {
                    movieSessionService.createSession(requestDTO);
                }
        );

        assertEquals("Sala não encontrada com o id: " + requestDTO.getTheaterId(), exception.getMessage());
        verify(movieSessionRepository, never()).save(any(MovieSession.class));
    }
}
