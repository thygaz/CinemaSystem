package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.model.Payment;
import br.com.cinema.cinemasystem.model.PaymentStatus;
import br.com.cinema.cinemasystem.model.Purchase;
import br.com.cinema.cinemasystem.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Simula o processamento de um pagamento.
     * Retorna um objeto Payment com status APPROVED ou DECLINED.
     */
    public Payment processPayment(Purchase purchase, String paymentMethod, BigDecimal totalAmount) {
        Payment payment = new Payment();

        payment.setPurchase(purchase);
        payment.setAmount(totalAmount);
        payment.setPaymentMethod(paymentMethod);

        // Simula um gateway de pagamento
        boolean approved = new Random().nextBoolean();
        payment.setStatus(approved ? PaymentStatus.APPROVED : PaymentStatus.DECLINED);

        // Gera um ID de transação aleatório (mock)
        payment.setTransactionId(UUID.randomUUID().toString());

        // Salva no banco de dados
        return paymentRepository.save(payment);
    }

    /**
     * Busca um pagamento pelo ID da compra ou do próprio pagamento.
     */
    public Payment getPaymentDetails(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado para o ID: " + paymentId));
    }
}
