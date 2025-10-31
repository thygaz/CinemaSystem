package br.com.cinema.cinemasystem.repository;

import br.com.cinema.cinemasystem.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    Seat findSeatById(Long id);
}
