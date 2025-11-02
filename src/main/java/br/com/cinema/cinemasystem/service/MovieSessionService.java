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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieSessionService {

    private final MovieSessionRepository movieSessionRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    public MovieSessionService(MovieSessionRepository movieSessionRepository, MovieRepository movieRepository, TheaterRepository theaterRepository) {
        this.movieSessionRepository = movieSessionRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
    }

    public MovieSessionResponseDTO createSession(MovieSessionRequestDTO requestDTO){

        Movie movie = movieRepository.findById(requestDTO.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Filme não encontrado com o id: " + requestDTO.getMovieId()));

        Theater theater = theaterRepository.findById(requestDTO.getTheaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Sala não encontrada com o id: " + requestDTO.getTheaterId()));


        MovieSession newSession = new MovieSession();
        newSession.setMovie(movie);
        newSession.setTheater(theater);
        newSession.setSessionTime(requestDTO.getSessionTime());
        newSession.setTicketPrice(requestDTO.getTicketPrice());

        MovieSession savedSession = movieSessionRepository.save(newSession);

        return convertToResponseDTO(savedSession);
    }

    public MovieSession findMovieSessionById(Long id){
        return movieSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sessão não encontrado com o id " + id));
    }

    public List<MovieSession> getAllSessions(){
        return movieSessionRepository.findAll();
    }

    public MovieSession updateSession(Long id, MovieSession sessionDetails){
        MovieSession updatedSession = findMovieSessionById(id);

        if(sessionDetails.getSessionTime() != null){
            updatedSession.setSessionTime(sessionDetails.getSessionTime());

        }
        if(sessionDetails.getMovie() != null){
            updatedSession.setMovie(sessionDetails.getMovie());
        }


        return movieSessionRepository.save(updatedSession);
    }

    public void deleteSession(Long id){
        MovieSession deleteSession = findMovieSessionById(id);
        movieSessionRepository.delete(deleteSession);
    }

    private MovieSessionResponseDTO convertToResponseDTO(MovieSession session){

        Movie movie = session.getMovie();
        Theater theater = session.getTheater();

        return new MovieSessionResponseDTO(
                session.getId(),
                session.getSessionTime(),
                session.getTicketPrice(),
                (movie != null) ? movie.getTitle() : null,
                (movie != null) ? movie.getGenre() : null,
                (movie != null) ? movie.getDurationInMinutes() : null,

                (theater != null) ? theater.getName() : null
                );
    }
}
