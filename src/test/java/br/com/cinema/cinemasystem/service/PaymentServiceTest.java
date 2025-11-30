package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.model.Payment;
import br.com.cinema.cinemasystem.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentRepository paymentRepository;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentRepository = Mockito.mock(PaymentRepository.class);
        paymentService = new PaymentService(paymentRepository);
    }

    @Test
    void testGetAllPayments_ReturnsListOfPayments() {
        // Arrange
        Payment p1 = new Payment();
        p1.setId(1L);
        p1.setAmount(50.0);
        p1.setStatus("SUCCESS");

        Payment p2 = new Payment();
        p2.setId(2L);
        p2.setAmount(75.0);
        p2.setStatus("SUCCESS");

        List<Payment> mockList = Arrays.asList(p1, p2);

        when(paymentRepository.findAll()).thenReturn(mockList);

        // Act
        List<Payment> result = paymentService.getAllPayments();

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(75.0, result.get(1).getAmount());

        verify(paymentRepository, times(1)).findAll();
    }
}