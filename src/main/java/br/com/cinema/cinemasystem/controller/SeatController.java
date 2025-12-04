package br.com.cinema.cinemasystem.controller;

import br.com.cinema.cinemasystem.dto.seat.SeatAvailabilityDTO;
import br.com.cinema.cinemasystem.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // Endpoint solicitado no Plano (Dia 7): GET /sessions/{sessionId}/seats
    @GetMapping("/sessions/{sessionId}/seats")
    public ResponseEntity<List<SeatAvailabilityDTO>> getSeatsBySession(@PathVariable Long sessionId) {
        List<SeatAvailabilityDTO> seats = seatService.getSeatsBySession(sessionId);
        return ResponseEntity.ok(seats);
    }
}