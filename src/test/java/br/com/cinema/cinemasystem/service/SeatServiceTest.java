package br.com.cinema.cinemasystem.service;


import br.com.cinema.cinemasystem.dto.seat.SeatStatus;
import br.com.cinema.cinemasystem.exception.ResourceNotFoundException;
import br.com.cinema.cinemasystem.model.Seat;
import br.com.cinema.cinemasystem.model.Theater;
import br.com.cinema.cinemasystem.model.User;
import br.com.cinema.cinemasystem.repository.SeatRepository;
import br.com.cinema.cinemasystem.repository.TheaterRepository;
import br.com.cinema.cinemasystem.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private TheaterRepository theaterRepository;

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private SeatService seatService;

    private User testUser;
    private Seat availableSeat;
    private Seat availableSeat2;
    private Seat lockedSeat;
    private Theater testTheater;


    @BeforeEach
    void setup(){
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Userteste");

        availableSeat = new Seat();
        availableSeat.setId(10L);
        availableSeat.setStatus(SeatStatus.AVAILABLE);

        availableSeat2 = new Seat();
        availableSeat2.setId(11L);
        availableSeat2.setStatus(SeatStatus.AVAILABLE);

        lockedSeat = new Seat();
        lockedSeat.setId(12L);
        lockedSeat.setStatus(SeatStatus.LOCKED);

        testTheater = new Theater();
        testTheater.setId(1L);
        testTheater.setSeats(List.of(availableSeat, availableSeat2, lockedSeat));
    }


    @Test
    public void deveEncontrarAssentosPorSalaComSucesso(){

        when(theaterRepository.findById(1L)).thenReturn(Optional.of(testTheater));


        List<Seat> result = seatService.findSeatsByTheater(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(10L, result.get(0).getId());
    }

    @Test
    public void deveLancarExcecaoSalaNaoEncontrada(){
        when(theaterRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            seatService.findSeatsByTheater(99L);
        });
    }


    @Test
    public void deveBloquearAssentosComSucesso(){
        List<Long> seatIdsToLock = List.of(10L, 11L);
        List<Seat> seatsToLock = List.of(availableSeat, availableSeat2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        when(seatRepository.findAllById(seatIdsToLock)).thenReturn(seatsToLock);

        when(seatRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Seat> lockedSeats = seatService.lockSeats(seatIdsToLock, 1L);

        Assertions.assertNotNull(lockedSeats);
        Assertions.assertEquals(2, lockedSeats.size());
        Assertions.assertEquals(SeatStatus.LOCKED, lockedSeats.get(0).getStatus());
        Assertions.assertEquals(SeatStatus.LOCKED, lockedSeats.get(1).getStatus());
        Assertions.assertEquals(testUser, lockedSeats.get(0).getLockingUser());

        verify(seatRepository, times(1)).saveAll(seatsToLock);
    }
}
