package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.model.Payment;
import br.com.cinema.cinemasystem.model.PaymentStatus;
import br.com.cinema.cinemasystem.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment processPayment(Long userId, String method, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setPaymentMethod(method);
        payment.setPaymentDate(LocalDateTime.now());

        if ("CARTAO_INVALIDO".equals(method) || amount.compareTo(BigDecimal.ZERO) <= 0) {
            payment.setStatus(PaymentStatus.DECLINED);
        } else {
            payment.setStatus(PaymentStatus.APPROVED);
        }

        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}