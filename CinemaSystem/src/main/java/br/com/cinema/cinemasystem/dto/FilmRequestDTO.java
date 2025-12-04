package br.com.cinema.cinemasystem.dto;

import jakarta.validation.constraints.NotBlank;

public record FilmRequestDTO(

        @NotBlank(message = "O nome do filme é obrigatório.")
        String name
) {
}
