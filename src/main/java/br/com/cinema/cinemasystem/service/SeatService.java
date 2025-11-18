package br.com.cinema.cinemasystem.service;


import br.com.cinema.cinemasystem.exception.ResourceNotFoundException;
import br.com.cinema.cinemasystem.exception.SeatNotAvailableException;
import br.com.cinema.cinemasystem.model.Seat;
import br.com.cinema.cinemasystem.model.Theater;
import br.com.cinema.cinemasystem.model.User;
import br.com.cinema.cinemasystem.repository.SeatRepository;
import br.com.cinema.cinemasystem.repository.TheaterRepository;
import br.com.cinema.cinemasystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import br.com.cinema.cinemasystem.dto.seat.SeatStatus;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final TheaterRepository theaterRepository;
    private final UserRepository userRepository;

    public SeatService(SeatRepository seatRepository, TheaterRepository theaterRepository, UserRepository userRepository) {
        this.seatRepository = seatRepository;
        this.theaterRepository = theaterRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<Seat> lockSeats(List<Long> seatIds, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário " + userId + " não encontrado"));

        List<Seat> seats = seatRepository.findAllById(seatIds);

        if (seats.size() != seatIds.size()) {
            throw new ResourceNotFoundException("Um ou mais assentos não foram encontrados.");

        }

        for (Seat seat : seats) {
            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                throw new SeatNotAvailableException(
                        "O assento " + seat.getId() + "não está mais disponível."
                );
            }
        }

        for (Seat seat : seats) {
            seat.setStatus(SeatStatus.LOCKED);
            seat.setLockingUser(user);
        }
        return seatRepository.saveAll(seats);
    }

    @Transactional
    public void releaseSeats(List<Long> seatIds) {
        List<Seat> seats = seatRepository.findAllById(seatIds);

        for (Seat seat : seats) {
            if (seat.getStatus() == SeatStatus.LOCKED) {
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setLockingUser(null);
            }
        }
        seatRepository.saveAll(seats);
    }

    public List<Seat> findSeatsByTheater(Long theaterId) {

        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Sala não encontrada com o id:" + theaterId));

        return theater.getSeats();
    }
}
