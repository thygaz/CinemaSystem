//package br.com.cinema.cinemasystem.service;
//
//
//import br.com.cinema.cinemasystem.model.Seat;
//import br.com.cinema.cinemasystem.repository.SeatRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class SeatService {
//
//    @Autowired
//    private final SeatRepository seatRepository;
//
//    public SeatService(SeatRepository seatRepository){
//        this.seatRepository = seatRepository;
//    }
//
//    public Seat createSeat(Seat seat){
//        return seatRepository.save(seat);
//    }
//
//    public Seat findSeatById(Long id) {
//        return seatRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Assento não encontrado com o Id: "+ id));
//    }
//
//    public void releaseSeats(List<Long> seatIds) {
//    }
//
////    public List<Seat> lockSeats(List<Long> seatIds, Long id) {
////        return
////    }
//}
