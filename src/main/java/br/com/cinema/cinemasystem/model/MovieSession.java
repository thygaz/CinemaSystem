package br.com.cinema.cinemasystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sessions")
public class MovieSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime sessionTime;

    @Column
    private double ticketPrice;

    @ManyToOne
    @JoinColumn(name = "movies_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    private Theater theater;

    @OneToMany
    @JoinColumn(name = "seats_id", nullable = false)
    private List<Seat> seats;


}