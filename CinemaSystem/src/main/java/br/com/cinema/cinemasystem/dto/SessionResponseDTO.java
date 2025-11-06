package br.com.cinema.cinemasystem.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionResponseDTO(

        UUID sessionUuid,
        FilmResponseDTO filmResponseDTO,
        LocalDateTime dateTime,
        Double price
) {
}
