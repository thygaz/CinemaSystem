package br.com.cinema.cinemasystem.service;


import br.com.cinema.cinemasystem.model.Seat;
import br.com.cinema.cinemasystem.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    public Seat createSeat(Seat seat){
        return seatRepository.save(seat);
    }

    public Seat findSeatById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assento não encontrado com o Id: "+ id));
    }

}
