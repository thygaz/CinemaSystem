package br.com.cinema.cinemasystem.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PurchaseDTO {
    private Long id;
    private LocalDateTime purchaseTimestamp;
    private String userName;

    private MovieSessionDTO movieSession;

    private Set<SeatDTO> seats;
}