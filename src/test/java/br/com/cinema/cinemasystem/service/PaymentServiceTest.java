package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.model.Payment;
import br.com.cinema.cinemasystem.model.PaymentStatus;
import br.com.cinema.cinemasystem.model.Purchase;
import br.com.cinema.cinemasystem.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
    }

    @Test
    void testGetAllPayments_Success() {
        // Arrange
        Payment p1 = new Payment();
        p1.setId(1L);
        p1.setAmount(new BigDecimal("50.00"));
        p1.setStatus(PaymentStatus.APPROVED);

        Payment p2 = new Payment();
        p2.setId(2L);
        p2.setAmount(new BigDecimal("75.90"));
        p2.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        // Service with default approval supplier (not used here)
        PaymentService service = new PaymentService(paymentRepository, () -> true);

        // Act
        List<Payment> result = service.getAllPayments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(p1, result.get(0));
        assertEquals(p2, result.get(1));
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void testProcessPayment_Success() {
        // Arrange: repository returns the same payment but with ID set to simulate DB behavior
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setId(100L);
            return p;
        });

        // Force approval via supplier
        PaymentService service = new PaymentService(paymentRepository, () -> true);

        Purchase mockPurchase = mock(Purchase.class);
        BigDecimal amount = new BigDecimal("25.50");

        // Act
        Payment saved = service.processPayment(mockPurchase, "CREDIT_CARD", amount);

        // Assert
        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(1)).save(captor.capture());
        Payment captured = captor.getValue();

        assertNotNull(saved);
        assertEquals(captured, saved);
        assertEquals(PaymentStatus.APPROVED, captured.getStatus());
        assertEquals("CREDIT_CARD", captured.getPaymentMethod());
        assertEquals(amount, captured.getAmount());
        assertNotNull(captured.getTransactionId());
        assertEquals(100L, saved.getId().longValue());
    }

    @Test
    void testProcessPayment_Fail_PaymentDeclined() {
        // Arrange: repository simulate save
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setId(101L);
            return p;
        });

        // Force decline via supplier
        PaymentService service = new PaymentService(paymentRepository, () -> false);

        Purchase mockPurchase = mock(Purchase.class);
        BigDecimal amount = new BigDecimal("42.00");

        // Act
        Payment result = service.processPayment(mockPurchase, "PIX", amount);

        // Assert
        assertNotNull(result);
        assertEquals(PaymentStatus.DECLINED, result.getStatus());
        assertEquals("PIX", result.getPaymentMethod());
        assertEquals(amount, result.getAmount());
        assertNotNull(result.getTransactionId());
        assertEquals(101L, result.getId().longValue());

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testGetPaymentDetails_Success() {
        // Arrange
        Payment p = new Payment();
        p.setId(50L);
        p.setAmount(new BigDecimal("10.00"));
        p.setStatus(PaymentStatus.APPROVED);

        when(paymentRepository.findById(50L)).thenReturn(Optional.of(p));
        PaymentService service = new PaymentService(paymentRepository, () -> true);

        // Act
        Payment found = service.getPaymentDetails(50L);

        // Assert
        assertNotNull(found);
        assertEquals(50L, found.getId().longValue());
        verify(paymentRepository, times(1)).findById(50L);
    }

    @Test
    void testGetPaymentDetails_NotFound_ShouldThrow() {
        // Arrange
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());
        PaymentService service = new PaymentService(paymentRepository, () -> true);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getPaymentDetails(999L));
        assertTrue(ex.getMessage().contains("Pagamento não encontrado"));
        verify(paymentRepository, times(1)).findById(999L);
    }
}
