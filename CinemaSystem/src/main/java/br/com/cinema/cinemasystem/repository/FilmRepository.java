package br.com.cinema.cinemasystem.repository;

import br.com.cinema.cinemasystem.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FilmRepository extends JpaRepository<Film, UUID> {

}
