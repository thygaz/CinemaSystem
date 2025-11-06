package br.com.cinema.cinemasystem.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID uuid;

    @ManyToOne
    @JoinColumn
    private Film film;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "price", nullable = false)
    private double price;

    public Session() {
    }

    public Session(Film film, LocalDateTime dateTime, double price) {
        this.film = film;
        this.dateTime = dateTime;
        this.price = price;
    }

//    public Session(UUID uuid, Film film, LocalDateTime dateTime, double price) {
//        this.uuid = uuid;
//        this.film = film;
//        this.dateTime = dateTime;
//        this.price = price;
//    }

    public UUID getUuid() {
        return uuid;
    }

    public Film getFilm() {
        return film;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public double getPrice() {
        return price;
    }
}
