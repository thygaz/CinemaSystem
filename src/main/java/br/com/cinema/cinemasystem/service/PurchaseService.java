//package br.com.cinema.cinemasystem.service;
//
//import br.com.cinema.cinemasystem.dto.PurchaseRequestDTO;
//import br.com.cinema.cinemasystem.model.*;
//import br.com.cinema.cinemasystem.repository.*;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.HashSet;
//import java.util.List;
//
//@Service
//public class PurchaseService {
//
//    private final PurchaseRepository purchaseRepository;
//    private final UserRepository userRepository;
//    private final MovieSessionRepository movieSessionRepository;
//    private final SeatService seatService;
//    private final PaymentService paymentService;
//    private final TicketService ticketService;
//
//    public PurchaseService(PurchaseRepository purchaseRepository,
//                           UserRepository userRepository,
//                           MovieSessionRepository movieSessionRepository,
//                           SeatService seatService,
//                           PaymentService paymentService,
//                           TicketService ticketService) {
//        this.purchaseRepository = purchaseRepository;
//        this.userRepository = userRepository;
//        this.movieSessionRepository = movieSessionRepository;
//        this.seatService = seatService;
//        this.paymentService = paymentService;
//        this.ticketService = ticketService;
//    }
//
//    @Transactional
//    public Purchase createPurchase(PurchaseRequestDTO requestDTO) {
//
//        User user = userRepository.findById(requestDTO.getUserId())
//                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + requestDTO.getUserId()));
//
//        MovieSession session = movieSessionRepository.findById(requestDTO.getMovieSessionId())
//                .orElseThrow(() -> new RuntimeException("Sessão não encontrada: " + requestDTO.getUserId()));
//
//        List<Long> seatIds = requestDTO.getSeatIds();
//
//        List<Seat> lockedSeats;
//        try {
//            lockedSeats = seatService.lockSeats(seatIds, session.getId());
//        } catch (Exception e) {
//            throw new RuntimeException("Assentos não disponíveis. " + e.getMessage());
//        }
//
//        Payment payment;
//        try {
//            double totalAmount = calculateTotal(lockedSeats, session);
//            payment = paymentService.processPayment(user.getId(), requestDTO.getPaymentMethod(), totalAmount);
//
//        } catch (Exception e) {
//            seatService.releaseSeats(seatIds);
//            throw new RuntimeException("Pagamento falhou. Compra cancelada. " + e.getMessage());
//        }
//
//        if ("APPROVED".equals(payment.getStatus())) {
//
//            Purchase newPurchase = new Purchase();
//            newPurchase.setUser(user);
//            newPurchase.setMovieSession(session);
//            newPurchase.setSeats(new HashSet<>(lockedSeats));
//            newPurchase.setPurchaseTimestamp(LocalDateTime.now());
//
//            Purchase savedPurchase = purchaseRepository.save(newPurchase);
//
//            ticketService.generateTickets(savedPurchase);
//
//            return savedPurchase;
//
//        } else {
//            seatService.releaseSeats(seatIds);
//            throw new RuntimeException("Pagamento foi recusado. Tente novamente.");
//        }
//    }
//
//    private Double calculateTotal(List<Seat> seats, MovieSession session) {
//        return seats.size() * session.getTicketPrice();
//    }
//
//    //
//    // ... Seus outros métodos (findPurchaseById, findPurchasesByUserId, etc.) ...
//    //
//}