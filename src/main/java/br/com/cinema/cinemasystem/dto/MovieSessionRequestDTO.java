package br.com.cinema.cinemasystem.dto;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Data
public class MovieSessionRequestDTO {

    private Long movieId;

    private Long theaterId;

    private LocalDateTime sessionTime;

    private Double ticketPrice;

}
