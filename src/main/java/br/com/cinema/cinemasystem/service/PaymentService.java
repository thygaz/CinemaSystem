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
     * Construtor padrão usado pelo Spring (usa Random para decidir aprovação).
     */
    public PaymentService(PaymentRepository paymentRepository) {
        this(paymentRepository, () -> new Random().nextBoolean());
    }

    /**
     * Construtor adicional que permite injetar um Supplier<Boolean> para determinar
     * se o pagamento será aprovado — útil para testes unitários determinísticos.
     */
    public PaymentService(PaymentRepository paymentRepository, Supplier<Boolean> approvalSupplier) {
        this.paymentRepository = paymentRepository;
        this.approvalSupplier = approvalSupplier;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    /**
     * Processa um pagamento "simulado".
     * @param purchase objeto Purchase associado ao pagamento
     * @param paymentMethod método de pagamento (ex: CREDIT_CARD, PIX)
     * @param totalAmount valor do pagamento
     * @return Payment persistido (com status APPROVED ou DECLINED)
     */
    public Payment processPayment(Purchase purchase, String paymentMethod, BigDecimal totalAmount) {
        Payment payment = new Payment();
        // associa a purchase (assumindo que Purchase já tem id quando existir)
        payment.setPurchase(purchase);
        payment.setAmount(totalAmount);
        payment.setPaymentMethod(paymentMethod);

        // decide aprovação via supplier (injetável)
        boolean approved = Boolean.TRUE.equals(approvalSupplier.get());
        payment.setStatus(approved ? PaymentStatus.APPROVED : PaymentStatus.DECLINED);

        // metadados (transactionId, timestamp)
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setCreatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    /**
     * Recupera um pagamento por id.
     * @param paymentId id do payment
     * @return Payment ou lança RuntimeException se não encontrado
     */
    public Payment getPaymentDetails(Long paymentId) {
        Optional<Payment> found = paymentRepository.findById(paymentId);
        return found.orElseThrow(() -> new RuntimeException("Pagamento não encontrado para o ID: " + paymentId));
    }
}