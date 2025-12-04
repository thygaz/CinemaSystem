package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.dto.purchase.PurchaseDTO;
import br.com.cinema.cinemasystem.dto.purchase.PurchaseRequestDTO;
import br.com.cinema.cinemasystem.model.*;
import br.com.cinema.cinemasystem.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final MovieSessionRepository movieSessionRepository;
    private final SeatService seatService;
    private final PaymentService paymentService;
    private final TicketService ticketService;

    public PurchaseService(PurchaseRepository purchaseRepository, UserRepository userRepository, MovieSessionRepository movieSessionRepository, SeatService seatService, PaymentService paymentService, TicketService ticketService) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.movieSessionRepository = movieSessionRepository;
        this.seatService = seatService;
        this.paymentService = paymentService;
        this.ticketService = ticketService;
    }

    @Transactional
    public PurchaseDTO createPurchase(PurchaseRequestDTO requestDTO) {

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        MovieSession session = movieSessionRepository.findById(requestDTO.getMovieSessionId())
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada"));

        List<Long> seatIds = requestDTO.getSeatIds();

        List<Seat> lockedSeats;
        try {
            lockedSeats = seatService.lockSeats(seatIds, session.getId());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao bloquear assentos: " + e.getMessage());
        }

        try {
            BigDecimal amountDecimal = BigDecimal.valueOf(calculateTotal(lockedSeats, session));

            Payment payment = paymentService.processPayment(
                    user.getId(),
                    requestDTO.getPaymentMethod(),
                    amountDecimal
            );

            if (payment.getStatus() != PaymentStatus.APPROVED) {
                seatService.releaseSeats(seatIds);
                throw new RuntimeException("Pagamento recusado.");
            }


        } catch (Exception e) {
            seatService.releaseSeats(seatIds);
            throw new RuntimeException("Falha no pagamento: " + e.getMessage());
        }

        Purchase newPurchase = new Purchase();
        newPurchase.setUser(user);
        newPurchase.setMovieSession(session);
        newPurchase.setSeats(new ArrayList<>(lockedSeats));
        newPurchase.setPurchaseTimestamp(LocalDateTime.now());

        Purchase savedPurchase = purchaseRepository.save(newPurchase);

        ticketService.generateTickets(savedPurchase);

        return new PurchaseDTO(savedPurchase);
    }

    @Transactional(readOnly = true)
    public PurchaseDTO findPurchaseById(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra não encontrada"));
        return new PurchaseDTO(purchase);
    }

    @Transactional(readOnly = true)
    public List<PurchaseDTO> findPurchasesByUserId(Long userId) {
        List<Purchase> purchases = purchaseRepository.findByUserId(userId);
        return purchases.stream()
                .map(PurchaseDTO::new)
                .collect(Collectors.toList());
    }

    private Double calculateTotal(List<Seat> seats, MovieSession session) {
        return seats.size() * session.getTicketPrice();
    }
}