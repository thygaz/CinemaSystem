package br.com.cinema.cinemasystem.model;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer durationInMinutes;

    @Column
    private String synopsis;

    @Column
    private String genre;

    @Column
    private String urlPoster;
}
