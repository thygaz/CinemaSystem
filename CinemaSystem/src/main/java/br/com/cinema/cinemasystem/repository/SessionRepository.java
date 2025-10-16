package br.com.cinema.cinemasystem.repository;

import br.com.cinema.cinemasystem.model.Film;
import br.com.cinema.cinemasystem.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    List<Session> findByFilm(Film film);
}
