package br.com.cinema.cinemasystem.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionRequestDTO(

        @NotNull(message = "O filme é obrigatório.")
        UUID filmUuid,

        @NotNull(message = "A data e hora são obrigatórias.")
        @Future(message = "A sessão deve ser em uma data futura.")
        LocalDateTime dateTime,

        @NotNull(message = "O preço é obrigatório.")
        @Positive(message = "O preço deve ser maior do que zero.")
        Double price
) {
}
