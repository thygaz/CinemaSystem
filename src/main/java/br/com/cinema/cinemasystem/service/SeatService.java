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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    // Injetamos o repo de sessão para buscar os assentos daquela sessão
    private final MovieSessionRepository movieSessionRepository;

    public SeatService(SeatRepository seatRepository,
                       UserRepository userRepository,
                       MovieSessionRepository movieSessionRepository) {
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
        this.movieSessionRepository = movieSessionRepository;
    }

    // --- Lógica da Fase 2: Visualizar Mapa de Assentos ---

    public List<SeatAvailabilityDTO> getSeatsBySession(Long sessionId) {
        // 1. Busca a sessão (Integração com o módulo do Maurício)
        MovieSession session = movieSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Sessão não encontrada com id: " + sessionId));

        // 2. A sessão tem uma lista de assentos (definida no Model MovieSession)
        // Convertemos para DTO para enviar ao front
        return session.getSeats().stream()
                .map(SeatAvailabilityDTO::new)
                .collect(Collectors.toList());
    }

    // --- Lógica da Fase 3: Integração com Purchase (Thiago) ---

    @Transactional
    public List<Seat> lockSeats(List<Long> seatIds, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário " + userId + " não encontrado"));

        List<Seat> seats = seatRepository.findAllById(seatIds);

        // Validação: Se a quantidade encontrada for diferente da solicitada, algum ID estava errado
        if (seats.size() != seatIds.size()) {
            throw new ResourceNotFoundException("Um ou mais assentos não foram encontrados.");
        }

        // Validação: Verifica se TODOS estão livres antes de bloquear qualquer um
        for (Seat seat : seats) {
            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                throw new SeatNotAvailableException(
                        "O assento " + seat.getRowIdentifier() + seat.getSeatNumber() + " não está disponível."
                );
            }
        }

        // Bloqueio efetivo
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
            // Só liberamos se estiver LOCKED. Se já estiver SOLD, não mexemos (segurança)
            if (seat.getStatus() == SeatStatus.LOCKED) {
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setLockingUser(null);
            }
        }
        seatRepository.saveAll(seats);
    }
}