package br.com.cinema.cinemasystem.service;


import br.com.cinema.cinemasystem.model.Movie;
import br.com.cinema.cinemasystem.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public Movie createMovie(Movie movie){
        return movieRepository.save(movie);
    }

    public Movie findMovieById(Long id){
        return movieRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Filme não encontrado com o id " + id));
    }

    public Movie updateMovie(Long id, Movie movieDetails){
        Movie existingMovie = findMovieById(id);

        if(movieDetails.getName() != null){
            existingMovie.setName(movieDetails.getName());
        }
        if(movieDetails.getDurationInMinutes() != null){
            existingMovie.setDurationInMinutes(movieDetails.getDurationInMinutes());
        }
        if(movieDetails.getSynopsis() != null){
            existingMovie.setSynopsis(movieDetails.getSynopsis());
        }
        return movieRepository.save(existingMovie);
    }

    public void deleteMovie(Long id) {
        Movie deleteMovie = movieRepository.findMovieById(id);
        movieRepository.delete(deleteMovie);
    }
}
