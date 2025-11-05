package br.com.cinema.cinemasystem.dto;

import java.util.UUID;

/*
DTO para retornar dados de um filme (contém o uuid)
 */
public record FilmResponseDTO(UUID uuid, String name) {
}
