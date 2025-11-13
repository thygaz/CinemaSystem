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

    // Listar tickets de um cliente específico
    @GetMapping("/users/{userId}/tickets")
    public ResponseEntity<List<Ticket>> listarTicketsDoCliente(@PathVariable Long userId) {
        List<Ticket> tickets = ticketService.listarTicketsPorCliente(userId);
        if (tickets.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content se não houver tickets
        }
        return ResponseEntity.ok(tickets); // 200 OK
    }

    // Criar ticket para um cliente específico
    @PostMapping("/users/{userId}/tickets")
    public ResponseEntity<Ticket> criarTicket(@PathVariable Long userId, @RequestParam String codigoIngresso) {
        try {
            Ticket ticket = ticketService.criarTicket(userId, codigoIngresso);
            return ResponseEntity.status(HttpStatus.CREATED).body(ticket); // 201 Created
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 se cliente ou ingresso não encontrado
        }
    }

    // Buscar ticket por ID
    @GetMapping("/tickets/{id}")
    public ResponseEntity<Ticket> buscarTicket(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.buscarPorId(id);
        return ticket.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build()); // 404 se não encontrado
    }

    // Deletar ticket por ID
    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<Void> deletarTicket(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.buscarPorId(id);
        if (ticket.isPresent()) {
            ticketService.deletarTicket(id);
            return ResponseEntity.noContent().build(); // 204 No Content após deletar
        } else {
            return ResponseEntity.notFound().build(); // 404 se não encontrado
        }
    }
}
