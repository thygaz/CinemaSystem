package br.com.cinema.cinemasystem.controller;

import br.com.cinema.cinemasystem.model.Ticket;
import br.com.cinema.cinemasystem.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/users/{userId}/tickets")
    public ResponseEntity<List<Ticket>> listarTicketsDoCliente(@PathVariable Long userId) {
        List<Ticket> tickets = ticketService.listarTicketsPorCliente(userId);
        if (tickets.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<Ticket> buscarTicket(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.buscarPorId(id);
        return ticket.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<Void> deletarTicket(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.buscarPorId(id);
        if (ticket.isPresent()) {
            ticketService.deletarTicket(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
