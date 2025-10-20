package br.com.cinema.cinemasystem.repository;

import br.com.cinema.cinemasystem.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Movie findMovieById(Long id);
}
