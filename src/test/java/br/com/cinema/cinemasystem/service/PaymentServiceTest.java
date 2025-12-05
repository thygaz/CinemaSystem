package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.model.Payment;
import br.com.cinema.cinemasystem.model.PaymentStatus;
import br.com.cinema.cinemasystem.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void testProcessPayment_Success() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setId(100L);
            return p;
        });

        BigDecimal amount = new BigDecimal("25.50");
        Long userId = 1L;

        Payment saved = paymentService.processPayment(userId, "CREDIT_CARD", amount);



        assertNotNull(saved);ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(1)).save(captor.capture());
        Payment captured = captor.getValue();
        assertEquals(PaymentStatus.APPROVED, captured.getStatus());
        assertEquals("CREDIT_CARD", captured.getPaymentMethod());
        assertEquals(amount, captured.getAmount());
        assertEquals(userId, captured.getUserId());
        assertEquals(100L, saved.getId().longValue());
    }

    @Test
    void testProcessPayment_Fail_PaymentDeclined() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setId(101L);
            return p;
        });

        BigDecimal amount = new BigDecimal("42.00");
        Long userId = 1L;

        Payment result = paymentService.processPayment(userId, "CARTAO_INVALIDO", amount);

        assertNotNull(result);
        assertEquals(PaymentStatus.DECLINED, result.getStatus());
        assertEquals("CARTAO_INVALIDO", result.getPaymentMethod());
        assertEquals(101L, result.getId().longValue());

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
}