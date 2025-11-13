package br.com.cinema.cinemasystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.cinema.cinemasystem.model.Cliente;
import br.com.cinema.cinemasystem.model.Ingressos;
import br.com.cinema.cinemasystem.model.Purchase;
import br.com.cinema.cinemasystem.model.Ticket;
import br.com.cinema.cinemasystem.repository.ClienteRepository;
import br.com.cinema.cinemasystem.repository.IngressosRepository;
import br.com.cinema.cinemasystem.repository.TicketRepository;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ClienteRepository clienteRepository;
    private final IngressosRepository ingressosRepository;

    public TicketService(
            TicketRepository ticketRepository,
            ClienteRepository clienteRepository,
            IngressosRepository ingressosRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.clienteRepository = clienteRepository;
        this.ingressosRepository = ingressosRepository;
    }

   
    public List<Ticket> listarTicketsPorCliente(Long clienteId) {
        return ticketRepository.findByClienteId(clienteId);
    }

    
    public Optional<Ticket> buscarPorId(Long id) {
        return ticketRepository.findById(id);
    }

    
    public Ticket criarTicket(Long clienteId, String codigoIngresso) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Ingressos ingresso = ingressosRepository.findById(codigoIngresso)
                .orElseThrow(() -> new RuntimeException("Ingresso não encontrado"));

        return gerarTicket(cliente, ingresso);
    }

    
    private Ticket gerarTicket(Cliente cliente, Ingressos ingresso) {
        ingresso.setStatus(Ingressos.Status.COMPRADO);
        ingressosRepository.save(ingresso);

        Ticket ticket = new Ticket(
                cliente,
                ingresso,
                LocalDateTime.now(),
                calcularValor(ingresso)
        );
        return ticketRepository.save(ticket);
    }

    
    public void generateTickets(Purchase purchase) {
        Cliente cliente = purchase.getCliente();

        for (Ingressos ingresso : purchase.getIngressos()) {
            gerarTicket(cliente, ingresso);
        }
    }

    
    public void deletarTicket(Long ticketId) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();
            Ingressos ingresso = ticket.getIngresso();
            ingresso.setStatus(Ingressos.Status.CANCELADO);
            ingressosRepository.save(ingresso);
            ticketRepository.deleteById(ticketId);
        }
    }

   
    private double calcularValor(Ingressos ingresso) {
        return ingresso.getQuantidade() * 25.0;
    }
}
