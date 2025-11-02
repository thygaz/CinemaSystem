package br.com.cinema.cinemasystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieSessionResponseDTO {

    private Long id;
    private LocalDateTime sessionTime;
    private Double ticketPrice;

    private String movieTitle;
    private String movieGenre;
    private Integer movieDurationInMinutes;

    private String theaterName;
}
