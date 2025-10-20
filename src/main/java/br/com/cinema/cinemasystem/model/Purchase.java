package br.com.cinema.cinemasystem.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_session_id", nullable = false)
    @ToString.Exclude
    private MovieSession movieSession;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "purchase_seats",
//            joinColumns = @JoinColumn(name = "purchase_id"),
//            inverseJoinColumns = @JoinColumn(name = "seat_id")
//    )
//    @ToString.Exclude
//    private Set<Seat> seats;

    @Column(nullable = false)
    private LocalDateTime purchaseTimestamp;
}