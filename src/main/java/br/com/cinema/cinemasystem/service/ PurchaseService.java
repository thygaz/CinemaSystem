//package br.com.cinema.cinemasystem.service;
//
//
//import com.cinemasystemspring.dto.CreatePurchaseRequestDTO;
//import com.cinemasystemspring.dto.MovieSessionDTO;
//import com.cinemasystemspring.dto.PurchaseDTO;
//import com.cinemasystemspring.dto.SeatDTO;
//import com.cinemasystemspring.model.*;
//import com.cinemasystemspring.repository.SeatRepository;
//import br.com.cinema.cinemasystem.repository.PurchaseRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//public class PurchaseService {
//
//    @Autowired
//    private PurchaseRepository purchaseRepository;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private MovieSessionService movieSessionService;
//
//    @Autowired
//    private SeatRepository seatRepository;
//
//    public PurchaseDTO createPurchase(CreatePurchaseRequestDTO requestDTO){
//
//        User user = userService.findUserEntityById(requestDTO.getUserId());
//        MovieSession session = movieSessionService.findSessionEntityById(requestDTO.getMovieSessionId());
//        Set<Long> requestSeatIds = requestDTO.getSeatIds();
//
//        Set<Long> occupiedSeatIds = purchaseRepository.findOccupiedIds(session.getId(), requestSeatIds);
//
//        if(!occupiedSeatIds.isEmpty()){
//            throw new IllegalStateException("Os seguintes assentos já estão ocupados: " + occupiedSeatIds);
//        }
//
//        Set<Seat> seats = new HashSet<>(seatRepository.findAllById(requestDTO.getSeatIds()));
//
//        Purchase newPurchase = new Purchase();
//        newPurchase.setUser(user);
//        newPurchase.setMovieSession(session);
//        newPurchase.setSeats(seats);
//        newPurchase.setPurchaseTimestamp(LocalDateTime.now());
//
//        Purchase savedPurchase = purchaseRepository.save(newPurchase);
//
//        return convertToDTO(savedPurchase);
//    }
//
//    @Transactional(readOnly = true)
//    public PurchaseDTO findPurchaseById(Long purchaseId){
//        Purchase purchase = purchaseRepository.findById(purchaseId)
//                .orElseThrow(() -> new RuntimeException("Compra não encontrada com o id " + purchaseId));
//
//        return convertToDTO(purchase);
//    }
//
//    @Transactional(readOnly = true)
//    public List<PurchaseDTO> findPurchasesByUserId(Long userId){
//        userService.findUserEntityById(userId);
//
//        return purchaseRepository.findByUserId(userId)
//                .stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//    private PurchaseDTO convertToDTO(Purchase purchase){
//        Set<SeatDTO> seatDTOs = purchase.getSeats().stream()
//                .map(seat -> {
//                    SeatDTO seatDTO = new SeatDTO();
//
//
//                    seatDTO.setId(seat.getId());
//                    seatDTO.setRowIdentifier(seat.getRowIdentifier());
//                    seatDTO.setSeatNumber(seat.getSeatNumber());
//
//                    return seatDTO;
//                })
//                .collect(Collectors.toSet());
//
//        MovieSessionDTO sessionDTO = movieSessionService.convertToDTO(purchase.getMovieSession());
//
//        PurchaseDTO purchaseDTO = new PurchaseDTO();
//        purchaseDTO.setId(purchase.getId());
//        purchaseDTO.setPurchaseTimestamp(purchase.getPurchaseTimestamp());
//        purchaseDTO.setUserName(purchase.getUser().getName());
//        purchaseDTO.setMovieSession(sessionDTO);
//        purchaseDTO.setSeats(seatDTOs);
//
//        return purchaseDTO;
//    }
//
//}