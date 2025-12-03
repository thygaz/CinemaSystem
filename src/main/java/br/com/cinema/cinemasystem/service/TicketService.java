package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.model.Purchase;
import br.com.cinema.cinemasystem.model.Ticket;
import br.com.cinema.cinemasystem.model.User;
import br.com.cinema.cinemasystem.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> listarTicketsPorCliente(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    public Optional<Ticket> buscarPorId(Long id) {
        return ticketRepository.findById(id);
    }

    public void generateTickets(Purchase purchase) {
        User user = purchase.getUser();

        purchase.getSeats().forEach(seat -> {
            Ticket ticket = new Ticket();
            ticket.setUser(user);
            ticket.setPurchase(purchase);
            ticket.setPurchaseTimestamp(LocalDateTime.now());
            ticket.setTotalValue(seat.getPrice());

            ticketRepository.save(ticket);
        });
    }

    public void deletarTicket(Long ticketId) {
        ticketRepository.findById(ticketId).ifPresent(ticketRepository::delete);
    }
}
