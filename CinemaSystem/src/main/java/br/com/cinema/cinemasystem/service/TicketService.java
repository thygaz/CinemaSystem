package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.model.Ticket;
import br.com.cinema.cinemasystem.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {

    private TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket save(Ticket ticket) {
        // precisa validar se sessão a existe
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> findById(UUID uuid) {
        return ticketRepository.findById(uuid);
    }
}
