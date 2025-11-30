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
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentRepository paymentRepository;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        // default approvalSupplier returning true — can be overridden in tests when needed
        Supplier<Boolean> approvalSupplier = () -> true;
        paymentService = new PaymentService(paymentRepository, approvalSupplier);
    }

    @Test
    void testGetAllPayments_Success() {
        Payment p1 = new Payment();
        p1.setId(1L);
        Payment p2 = new Payment();
        p2.setId(2L);

        when(paymentRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Payment> all = paymentService.getAllPayments();

        assertNotNull(all);
        assertEquals(2, all.size());
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void testProcessPayment_Success() {
        // Cria um Payment que será retornado pelo repository.save (simulando persistência)
        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment arg = invocation.getArgument(0);
            arg.setId(100L); // simula ID gerado pelo banco
            return arg;
        });

        // Forçamos aprovação: cria uma instância do service com supplier que retorna true
        PaymentService svc = new PaymentService(paymentRepository, () -> true);

        Purchase mockPurchase = mock(Purchase.class);
        BigDecimal amount = new BigDecimal("25.50");
        Payment result = svc.processPayment(mockPurchase, "CREDIT_CARD", amount);

        verify(paymentRepository, times(1)).save(captor.capture());
        Payment saved = captor.getValue();

        assertNotNull(result);
        assertEquals(saved, result);
        assertEquals(PaymentStatus.APPROVED, saved.getStatus());
        assertEquals("CREDIT_CARD", saved.getPaymentMethod());
        assertEquals(amount, saved.getAmount());
        assertNotNull(saved.getTransactionId());
        assertEquals(100L, saved.getId().longValue());
    }

    @Test
    void testProcessPayment_Fail_PaymentDeclined() {
        // Forçamos recusa: cria uma instância do service com supplier que retorna false
        PaymentService svc = new PaymentService(paymentRepository, () -> false);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment arg = invocation.getArgument(0);
            arg.setId(101L);
            return arg;
        });

        Purchase mockPurchase = mock(Purchase.class);
        BigDecimal amount = new BigDecimal("42.00");
        Payment result = svc.processPayment(mockPurchase, "PIX", amount);

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
        Payment p = new Payment();
        p.setId(50L);
        when(paymentRepository.findById(50L)).thenReturn(Optional.of(p));

        Payment found = paymentService.getPaymentDetails(50L);

        assertNotNull(found);
        assertEquals(50L, found.getId().longValue());
        verify(paymentRepository, times(1)).findById(50L);
    }

    @Test
    void testGetPaymentDetails_NotFound_ShouldThrow() {
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> paymentService.getPaymentDetails(999L));
        assertTrue(ex.getMessage().contains("Pagamento não encontrado"));
        verify(paymentRepository, times(1)).findById(999L);
    }
}