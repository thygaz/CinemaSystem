package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.dto.purchase.PurchaseDTO;
import br.com.cinema.cinemasystem.dto.purchase.PurchaseRequestDTO;
import br.com.cinema.cinemasystem.model.*;
import br.com.cinema.cinemasystem.repository.MovieSessionRepository;
import br.com.cinema.cinemasystem.repository.PurchaseRepository;
import br.com.cinema.cinemasystem.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MovieSessionRepository movieSessionRepository;
    @Mock
    private SeatService seatService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private TicketService ticketService;

    @InjectMocks
    private PurchaseService purchaseService;

    @Test
    @DisplayName("Deve realizar compra com sucesso quando tudo estiver OK")
    void deveCriarCompraComSucesso() {

        Long userId = 1L;
        Long sessionId = 10L;
        List<Long> seatIds = List.of(5L);
        Double precoIngresso = 20.0;

        PurchaseRequestDTO request = new PurchaseRequestDTO(userId, sessionId, seatIds, "CREDIT_CARD");


        User user = new User();
        user.setId(userId);

        MovieSession session = new MovieSession();
        session.setId(sessionId);
        session.setTicketPrice(precoIngresso);

        session.setMovie(new Movie(1L, "Filme Teste", 120, "Sinopse", "Ação", "url"));

        Seat seat = new Seat();
        seat.setId(5L);

        Payment paymentAprovado = new Payment();
        paymentAprovado.setStatus(PaymentStatus.APPROVED);

        Purchase purchaseSalva = new Purchase();
        purchaseSalva.setId(100L);
        purchaseSalva.setUser(user);
        purchaseSalva.setMovieSession(session);
        purchaseSalva.setSeats(List.of(seat));
        purchaseSalva.setPurchaseTimestamp(LocalDateTime.now());


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(movieSessionRepository.findById(sessionId)).thenReturn(Optional.of(session));


        when(seatService.lockSeats(seatIds, sessionId)).thenReturn(List.of(seat));


        when(paymentService.processPayment(eq(userId), eq("CREDIT_CARD"), any(BigDecimal.class)))
                .thenReturn(paymentAprovado);

        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchaseSalva);


        PurchaseDTO resultado = purchaseService.createPurchase(request);

        Assertions.assertNotNull(resultado);
        assertEquals(100L, resultado.getId());


        verify(ticketService, times(1)).generateTickets(any(Purchase.class));
    }

    @Test
    @DisplayName("Deve falhar se pagamento for recusado")
    void deveFalharPagamentoRecusado() {

        PurchaseRequestDTO request = new PurchaseRequestDTO(1L, 10L, Arrays.asList(5L), "CREDIT_CARD");

        User user = new User(); user.setId(1L);
        MovieSession session = new MovieSession(); session.setId(10L); session.setTicketPrice(50.0);
        Seat seat = new Seat(); seat.setId(5L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(movieSessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(seatService.lockSeats(any(), any())).thenReturn(Arrays.asList(seat));

        Payment paymentRecusado = new Payment();
        paymentRecusado.setStatus(PaymentStatus.DECLINED);
        when(paymentService.processPayment(any(), any(), any())).thenReturn(paymentRecusado);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> purchaseService.createPurchase(request));

        assertEquals("Falha no pagamento: Pagamento recusado.", ex.getMessage());

        verify(seatService, times(1)).releaseSeats(any());
    }
}