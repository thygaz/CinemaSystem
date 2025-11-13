package br.com.cinema.cinemasystem.dto;

import java.util.UUID;

public record TicketResponseDTO(UUID ticketUuid, SessionResponseDTO sessionResponseDTO) {
}
