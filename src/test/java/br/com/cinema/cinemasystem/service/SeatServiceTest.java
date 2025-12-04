package br.com.cinema.cinemasystem.service;

import br.com.cinema.cinemasystem.dto.seat.SeatAvailabilityDTO;
import br.com.cinema.cinemasystem.dto.seat.SeatStatus;
import br.com.cinema.cinemasystem.exception.ResourceNotFoundException;
import br.com.cinema.cinemasystem.exception.SeatNotAvailableException;
import br.com.cinema.cinemasystem.model.MovieSession;
import br.com.cinema.cinemasystem.model.Seat;
import br.com.cinema.cinemasystem.model.User;
import br.com.cinema.cinemasystem.repository.MovieSessionRepository;
import br.com.cinema.cinemasystem.repository.SeatRepository;
import br.com.cinema.cinemasystem.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieSessionRepository movieSessionRepository;

    @InjectMocks
    private SeatService seatService;

    private User usuarioTeste;
    private Seat assentoLivre;
    private Seat assentoOcupado;
    private MovieSession sessaoTeste;

    @BeforeEach
    void setup() {
        usuarioTeste = new User();
        usuarioTeste.setId(1L);
        usuarioTeste.setName("Vinicius");

        assentoLivre = new Seat();
        assentoLivre.setId(10L);
        assentoLivre.setRowIdentifier('A');
        assentoLivre.setSeatNumber(1);
        assentoLivre.setStatus(SeatStatus.AVAILABLE);

        assentoOcupado = new Seat();
        assentoOcupado.setId(11L);
        assentoOcupado.setRowIdentifier('A');
        assentoOcupado.setSeatNumber(2);
        assentoOcupado.setStatus(SeatStatus.LOCKED);

        sessaoTeste = new MovieSession();
        sessaoTeste.setId(100L);
        sessaoTeste.setSeats(List.of(assentoLivre, assentoOcupado));
    }


    @Test
    @DisplayName("Deve listar os assentos de uma sessão corretamente")
    void deveListarAssentosPorSessao() {
        when(movieSessionRepository.findById(100L)).thenReturn(Optional.of(sessaoTeste));

        List<SeatAvailabilityDTO> resultado = seatService.getSeatsBySession(100L);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2, resultado.size()); // Tinham 2 assentos na lista

        Assertions.assertEquals(SeatStatus.AVAILABLE, resultado.get(0).getStatus());
        Assertions.assertEquals(SeatStatus.LOCKED, resultado.get(1).getStatus());
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar sessão inexistente")
    void deveFalharAoListarSessaoInexistente() {
        when(movieSessionRepository.findById(999L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            seatService.getSeatsBySession(999L);
        });
    }


    @Test
    @DisplayName("Deve bloquear assentos livres com sucesso")
    void deveBloquearAssentos() {
        List<Long> idsParaBloquear = List.of(10L); // ID do assento livre

        when(userRepository.findById(1L)).thenReturn(Optional.of(usuarioTeste));
        when(seatRepository.findAllById(idsParaBloquear)).thenReturn(List.of(assentoLivre));
        when(seatRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Seat> assentosBloqueados = seatService.lockSeats(idsParaBloquear, 1L);

        Assertions.assertEquals(SeatStatus.LOCKED, assentosBloqueados.get(0).getStatus());
        Assertions.assertEquals(usuarioTeste, assentosBloqueados.get(0).getLockingUser());

        verify(seatRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Deve falhar ao tentar bloquear assento já ocupado")
    void naoDeveBloquearAssentoOcupado() {
        List<Long> idsParaBloquear = List.of(11L); // ID do assento OCUPADO

        when(userRepository.findById(1L)).thenReturn(Optional.of(usuarioTeste));
        when(seatRepository.findAllById(idsParaBloquear)).thenReturn(List.of(assentoOcupado));

        Assertions.assertThrows(SeatNotAvailableException.class, () -> {
            seatService.lockSeats(idsParaBloquear, 1L);
        });

        verify(seatRepository, never()).saveAll(anyList());
    }


    @Test
    @DisplayName("Deve liberar assentos (Rollback)")
    void deveLiberarAssentos() {
        List<Long> idsParaLiberar = List.of(11L);

        when(seatRepository.findAllById(idsParaLiberar)).thenReturn(List.of(assentoOcupado));

        seatService.releaseSeats(idsParaLiberar);

        Assertions.assertEquals(SeatStatus.AVAILABLE, assentoOcupado.getStatus());
        Assertions.assertNull(assentoOcupado.getLockingUser());

        verify(seatRepository, times(1)).saveAll(anyList());
    }
}