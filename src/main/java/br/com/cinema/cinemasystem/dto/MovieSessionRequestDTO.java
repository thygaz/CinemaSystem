package br.com.cinema.cinemasystem.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class MovieSessionRequestDTO {

    private Long movieId;

    private Long theaterId;

    private LocalDateTime sessionTime;

    private Double ticketPrice;

}
