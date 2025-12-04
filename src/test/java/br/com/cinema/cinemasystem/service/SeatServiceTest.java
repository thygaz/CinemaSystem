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

// @ExtendWith(MockitoExtension.class) é OBRIGATÓRIO para o Mockito funcionar com JUnit 5
@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    // @Mock cria uma versão "falsa" da classe, que não acessa o banco de verdade
    @Mock
    private SeatRepository seatRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieSessionRepository movieSessionRepository;

    // @InjectMocks pega a classe real que queremos testar e injeta os mocks nela
    @InjectMocks
    private SeatService seatService;

    // Variáveis para usar nos testes
    private User usuarioTeste;
    private Seat assentoLivre;
    private Seat assentoOcupado;
    private MovieSession sessaoTeste;

    // @BeforeEach roda ANTES de cada teste para "limpar a casa" e preparar os dados
    @BeforeEach
    void setup() {
        // Criando um usuário fake
        usuarioTeste = new User();
        usuarioTeste.setId(1L);
        usuarioTeste.setName("Vinicius");

        // Criando um assento LIVRE
        assentoLivre = new Seat();
        assentoLivre.setId(10L);
        assentoLivre.setRowIdentifier('A');
        assentoLivre.setSeatNumber(1);
        assentoLivre.setStatus(SeatStatus.AVAILABLE);

        // Criando um assento OCUPADO (LOCKED)
        assentoOcupado = new Seat();
        assentoOcupado.setId(11L);
        assentoOcupado.setRowIdentifier('A');
        assentoOcupado.setSeatNumber(2);
        assentoOcupado.setStatus(SeatStatus.LOCKED);

        // Criando uma sessão fake com esses assentos
        sessaoTeste = new MovieSession();
        sessaoTeste.setId(100L);
        sessaoTeste.setSeats(List.of(assentoLivre, assentoOcupado));
    }

    // --- CENÁRIO 1: Visualização (Fase 2) ---

    @Test
    @DisplayName("Deve listar os assentos de uma sessão corretamente")
    void deveListarAssentosPorSessao() {
        // ARRANGE (Preparação): Ensinamos o Mock o que fazer
        when(movieSessionRepository.findById(100L)).thenReturn(Optional.of(sessaoTeste));

        // ACT (Ação): Chamamos o método real
        List<SeatAvailabilityDTO> resultado = seatService.getSeatsBySession(100L);

        // ASSERT (Verificação): Conferimos se deu certo
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2, resultado.size()); // Tinham 2 assentos na lista

        // Verifica se converteu para DTO corretamente
        Assertions.assertEquals(SeatStatus.AVAILABLE, resultado.get(0).getStatus());
        Assertions.assertEquals(SeatStatus.LOCKED, resultado.get(1).getStatus());
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar sessão inexistente")
    void deveFalharAoListarSessaoInexistente() {
        // Simulamos que o banco não achou nada (Optional.empty)
        when(movieSessionRepository.findById(999L)).thenReturn(Optional.empty());

        // Verificamos se o serviço lança a exceção correta
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            seatService.getSeatsBySession(999L);
        });
    }

    // --- CENÁRIO 2: Bloqueio (Integração com Purchase) ---

    @Test
    @DisplayName("Deve bloquear assentos livres com sucesso")
    void deveBloquearAssentos() {
        List<Long> idsParaBloquear = List.of(10L); // ID do assento livre

        // Mocks necessários para o método lockSeats
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuarioTeste));
        when(seatRepository.findAllById(idsParaBloquear)).thenReturn(List.of(assentoLivre));
        // Quando pedir para salvar, retorna a própria lista que recebeu
        when(seatRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // Ação
        List<Seat> assentosBloqueados = seatService.lockSeats(idsParaBloquear, 1L);

        // Verificação
        Assertions.assertEquals(SeatStatus.LOCKED, assentosBloqueados.get(0).getStatus());
        Assertions.assertEquals(usuarioTeste, assentosBloqueados.get(0).getLockingUser());

        // Verifica se o método saveAll foi chamado 1 vez
        verify(seatRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Deve falhar ao tentar bloquear assento já ocupado")
    void naoDeveBloquearAssentoOcupado() {
        List<Long> idsParaBloquear = List.of(11L); // ID do assento OCUPADO

        when(userRepository.findById(1L)).thenReturn(Optional.of(usuarioTeste));
        when(seatRepository.findAllById(idsParaBloquear)).thenReturn(List.of(assentoOcupado));

        // O teste passa se essa exceção for lançada
        Assertions.assertThrows(SeatNotAvailableException.class, () -> {
            seatService.lockSeats(idsParaBloquear, 1L);
        });

        // IMPORTANTE: Garante que NADA foi salvo no banco, pois deu erro antes
        verify(seatRepository, never()).saveAll(anyList());
    }

    // --- CENÁRIO 3: Rollback (Pagamento falhou) ---

    @Test
    @DisplayName("Deve liberar assentos (Rollback)")
    void deveLiberarAssentos() {
        // Vamos tentar liberar o assento que está LOCKED (id 11)
        List<Long> idsParaLiberar = List.of(11L);

        when(seatRepository.findAllById(idsParaLiberar)).thenReturn(List.of(assentoOcupado));

        seatService.releaseSeats(idsParaLiberar);

        // Verifica se o status mudou para AVAILABLE
        Assertions.assertEquals(SeatStatus.AVAILABLE, assentoOcupado.getStatus());
        Assertions.assertNull(assentoOcupado.getLockingUser());

        // Verifica se salvou a alteração
        verify(seatRepository, times(1)).saveAll(anyList());
    }
}