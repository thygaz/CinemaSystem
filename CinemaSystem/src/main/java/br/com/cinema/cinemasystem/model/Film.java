package br.com.cinema.cinemasystem.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb_film")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID uuid;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Film() {
    }

    public Film(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

}
