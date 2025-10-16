package br.com.cinema.cinemasystem.controller;

import br.com.cinema.cinemasystem.model.Ticket;
import br.com.cinema.cinemasystem.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public Ticket save(@RequestBody Ticket ticket) {
        return ticketService.save(ticket);
    }

    @GetMapping(name = "/tickets/")
    public List<Ticket> listAll() {
        return ticketService.findAll();
    }

    @GetMapping(name = "/tickets/{uuid}")
    public ResponseEntity<?> findById(UUID uuid) {
        Optional<Ticket> ticket = ticketService.findById(uuid);

        return ResponseEntity.ok(ticket);
    }

    @DeleteMapping(name = "/tickets/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        ticketService.delete(uuid);

        return ResponseEntity.noContent().build();
    }
}
