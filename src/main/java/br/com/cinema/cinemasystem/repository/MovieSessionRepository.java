package br.com.cinema.cinemasystem.repository;

import br.com.cinema.cinemasystem.model.MovieSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieSessionRepository extends JpaRepository<MovieSession, Long> {
}
