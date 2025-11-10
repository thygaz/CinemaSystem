package br.com.cinema.cinemasystem.service;

import java.util.List;
import br.com.cinema.cinemasystem.model.Payment;
import br.com.cinema.cinemasystem.repository.PaymentRepository;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
