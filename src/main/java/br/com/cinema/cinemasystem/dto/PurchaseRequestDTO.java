package br.com.cinema.cinemasystem.dto;

import java.util.List;

 record PurchaseRequestDTO(
        Long userId,
        Long movieSessionId,
        List<Long> seatIds,
        String paymentMethod
) {
}