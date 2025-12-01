package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.model.Payment;
import br.com.cinema.cinemasystem.model.PaymentStatus;
import br.com.cinema.cinemasystem.model.Purchase;
import br.com.cinema.cinemasystem.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.Random;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final Supplier<Boolean> approvalSupplier;

    /**
     * Construtor usado pelo Spring — recebe apenas beans que o container conhece.
     * Ele delega para o construtor principal, injetando um Supplier padrão (Random).
     */
    public PaymentService(PaymentRepository paymentRepository) {
        this(paymentRepository, () -> new Random().nextBoolean());
    }

    /**
     * Construtor adicional para permitir injeção de um Supplier determinístico (útil para testes).
     */
    public PaymentService(PaymentRepository paymentRepository, Supplier<Boolean> approvalSupplier) {
        this.paymentRepository = paymentRepository;
        this.approvalSupplier = approvalSupplier;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment processPayment(Purchase purchase, String paymentMethod, BigDecimal totalAmount) {
        Payment payment = new Payment();

        payment.setPurchase(purchase);
        payment.setAmount(totalAmount);
        payment.setPaymentMethod(paymentMethod);

        boolean approved = Boolean.TRUE.equals(approvalSupplier.get());
        payment.setStatus(approved ? PaymentStatus.APPROVED : PaymentStatus.DECLINED);
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setCreatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public Payment getPaymentDetails(Long paymentId) {
        Optional<Payment> found = paymentRepository.findById(paymentId);
        return found.orElseThrow(() -> new RuntimeException("Pagamento não encontrado para o ID: " + paymentId));
    }
}