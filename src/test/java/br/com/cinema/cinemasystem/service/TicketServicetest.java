package br.com.cinema.cinemasystem.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import br.com.cinema.cinemasystem.model.Ticket;
import br.com.cinema.cinemasystem.repository.TicketRepository;

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
        // given
        Long userId = 1L;
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();

        when(ticketRepository.findByClienteId(userId)).thenReturn(Arrays.asList(ticket1, ticket2));

        // when
        List<Ticket> result = ticketService.listarTicketsPorCliente(userId);

        // then
        assertEquals(2, result.size());
        verify(ticketRepository, times(1)).findByClienteId(userId);
    }

    @Test
    void testGetTicketsByUserId_Fail_UserNotFound() {
        // given
        Long userId = 99L;
        when(ticketRepository.findByClienteId(userId)).thenReturn(List.of());

        // when
        List<Ticket> result = ticketService.listarTicketsPorCliente(userId);

        // then
        assertTrue(result.isEmpty());
        verify(ticketRepository, times(1)).findByClienteId(userId);
    }

    @Test
    void testBuscarTicketPorId_Success() {
        Long id = 1L;
        Ticket ticket = new Ticket();
        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticket));

        Optional<Ticket> result = ticketService.buscarPorId(id);

        assertTrue(result.isPresent());
        verify(ticketRepository, times(1)).findById(id);
    }
}
