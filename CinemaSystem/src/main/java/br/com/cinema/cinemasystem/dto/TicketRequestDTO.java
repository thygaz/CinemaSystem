package br.com.cinema.cinemasystem.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TicketRequestDTO(

        @NotNull(message = "A sessão é obrigatória.")
        UUID sessionUuid
) {
}
