package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.model.Purchase;
import br.com.cinema.cinemasystem.model.Seat;
import br.com.cinema.cinemasystem.model.Ticket;
import br.com.cinema.cinemasystem.model.User;
import br.com.cinema.cinemasystem.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTicketsByUserId_Success() {
        Long userId = 1L;
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();

        when(ticketRepository.findByUserId(userId)).thenReturn(Arrays.asList(ticket1, ticket2));

        List<Ticket> result = ticketService.listarTicketsPorCliente(userId);

        assertEquals(2, result.size());
        verify(ticketRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetTicketsByUserId_Fail_UserNotFound() {
        Long userId = 99L;
        when(ticketRepository.findByUserId(userId)).thenReturn(List.of());

        List<Ticket> result = ticketService.listarTicketsPorCliente(userId);

        assertTrue(result.isEmpty());
        verify(ticketRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testBuscarTicketPorId_Success() {
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        Optional<Ticket> result = ticketService.buscarPorId(ticketId);

        assertTrue(result.isPresent());
        verify(ticketRepository, times(1)).findById(ticketId);
    }

    @Test
    void testGenerateTickets_Success() {
        // Cria um usuário e assentos simulados
        User user = new User();
        user.setId(1L);

        Seat seat1 = new Seat();
        seat1.setId(1L);
        seat1.setPrice(50.0);

        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setPrice(30.0);

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setSeats(new HashSet<>(Arrays.asList(seat1, seat2)));

        // Simula o comportamento do save para retornar o mesmo objeto
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ticketService.generateTickets(purchase);

        // Verifica se salvou os tickets
        verify(ticketRepository, times(2)).save(any(Ticket.class));
    }

    @Test
    void testDeletarTicket_Success() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        ticketService.deletarTicket(1L);

        verify(ticketRepository, times(1)).delete(ticket);
    }

    @Test
    void testDeletarTicket_NotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        ticketService.deletarTicket(1L);

        verify(ticketRepository, never()).delete(any(Ticket.class));
    }
}
