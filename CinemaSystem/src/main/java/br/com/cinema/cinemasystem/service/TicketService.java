package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.dto.SessionResponseDTO;
import br.com.cinema.cinemasystem.dto.TicketRequestDTO;
import br.com.cinema.cinemasystem.dto.TicketResponseDTO;
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

    public TicketResponseDTO create(TicketRequestDTO ticketRequestDTO) {
        Session session = sessionRepository.findById(ticketRequestDTO.sessionUuid())
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada"));

        Ticket ticket = new Ticket(ticketRequestDTO.sessionUuid(), session);

        Ticket savedTicket = ticketRepository.save(ticket);

        SessionResponseDTO sessionResponseDTO = new SessionResponseDTO(session.getUuid(), session.getFilm(), session.getDateTime(), session.getPrice());

        return new TicketResponseDTO(savedTicket.getUuid(),
                sessionResponseDTO);
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public List<TicketResponseDTO> findAll() {
        return ticketRepository.findAll()
                .stream()
                .map(ticket -> new TicketResponseDTO(ticket.getUuid(),
                        new SessionResponseDTO(
                                ticket.getSession().getUuid(),
                                ticket.getSession().getFilm(),
                                ticket.getSession().getDateTime(),
                                ticket.getSession().getPrice()
                        )))
                .toList();
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
