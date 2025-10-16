package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.model.Session;
import br.com.cinema.cinemasystem.model.Ticket;
import br.com.cinema.cinemasystem.repository.SessionRepository;
import br.com.cinema.cinemasystem.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {

    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository, SessionRepository sessionRepository) {
        this.ticketRepository = ticketRepository;
        this.sessionRepository = sessionRepository;
    }

    public Ticket save(Ticket ticket) {
        // precisa validar se sessão a existe
        Session session = sessionRepository.findById(ticket.getSession().getUuid())
                .orElseThrow(() -> new RuntimeException("Não foi possível criar o ingresso, sessão não encontrada"));

        return ticketRepository.save(ticket);
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> findById(UUID uuid) {
        return ticketRepository.findById(uuid);
    }

    public void delete(UUID uuid) {
        if (ticketRepository.existsById(uuid)) {
            ticketRepository.deleteById(uuid);
        } else {
            throw new RuntimeException("Ticket não encontrado.");
        }
    }
}
