package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.model.Payment;
import br.com.cinema.cinemasystem.model.PaymentStatus;
import br.com.cinema.cinemasystem.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllPaymentsSuccessfully() {
        // Arrange
        Payment payment1 = new Payment();
        payment1.setId(1L);
        payment1.setAmount(new BigDecimal("50.00"));
        payment1.setStatus(PaymentStatus.PAID);

        Payment payment2 = new Payment();
        payment2.setId(2L);
        payment2.setAmount(new BigDecimal("75.90"));
        payment2.setStatus(PaymentStatus.PENDING);

        List<Payment> expectedPayments = Arrays.asList(payment1, payment2);

        when(paymentRepository.findAll()).thenReturn(expectedPayments);

        // Act
        List<Payment> result = paymentService.getAllPayments();

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedPayments, result);
    }
}
