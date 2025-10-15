package br.com.cinema.cinemasystem.repository;

import com.cinemasystemspring.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;


@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("SELECT s.id FROM Purchase p JOIN p.seats s WHERE p.movieSession.id = :sessionId AND s.id IN :seatIds")
    Set<Long> findOccupiedIds(
            @Param("sessionId") Long sessionId,
            @Param("seatIds") Set<Long> seatIds
    );

    List<Purchase> findByUserId(Long userId);
}