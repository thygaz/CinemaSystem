package br.com.cinema.cinemasystem.integration;

import br.com.cinema.cinemasystem.dto.purchase.PurchaseRequestDTO;
import br.com.cinema.cinemasystem.dto.seat.SeatStatus;
import br.com.cinema.cinemasystem.model.*;
import br.com.cinema.cinemasystem.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private TheaterRepository theaterRepository;
    @Autowired private MovieSessionRepository sessionRepository;
    @Autowired private SeatRepository seatRepository;
    @Autowired private PurchaseRepository purchaseRepository;

    private User user;
    private MovieSession session;
    private Seat seat1;
    private Seat seat2;

    @BeforeEach
    void setUp() {

        tearDown();

        user = new User();
        user.setName("Teste Integracao");
        user.setEmail("integracao@cinema.com");
        user.setPassword("123456");
        user = userRepository.save(user);

        Movie movie = new Movie();
        movie.setTitle("Filme de Teste");
        movie.setDurationInMinutes(120);
        movie = movieRepository.save(movie);

        Theater theater = new Theater();
        theater.setName("Sala 1");
        theater.setCapacity(100);
        theater = theaterRepository.save(theater);

        session = new MovieSession();
        session.setMovie(movie);
        session.setTheater(theater);
        session.setSessionTime(LocalDateTime.now().plusDays(1));
        session.setTicketPrice(25.50);
        session = sessionRepository.save(session);

        seat1 = new Seat();
        seat1.setRowIdentifier('A');
        seat1.setSeatNumber(1);
        seat1.setTheater(theater);
        seat1.setStatus(SeatStatus.AVAILABLE);
        seat1 = seatRepository.save(seat1);

        seat2 = new Seat();
        seat2.setRowIdentifier('A');
        seat2.setSeatNumber(2);
        seat2.setTheater(theater);
        seat2.setStatus(SeatStatus.AVAILABLE);
        seat2 = seatRepository.save(seat2);

        user = userRepository.save(user);

        session = sessionRepository.save(session);

        seat1 = seatRepository.save(seat1);
        seat2 = seatRepository.save(seat2);

        userRepository.flush();
        sessionRepository.flush();
        seatRepository.flush();
    }

    @AfterEach
    void tearDown() {
        purchaseRepository.deleteAll();
        seatRepository.deleteAll();
        sessionRepository.deleteAll();
        theaterRepository.deleteAll();
        movieRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve realizar uma compra com sucesso (Status 201)")
    void deveCriarCompraComSucesso() throws Exception {
        PurchaseRequestDTO request = new PurchaseRequestDTO();
        request.setUserId(user.getId());
        request.setMovieSessionId(session.getId());
        request.setSeatIds(Arrays.asList(seat1.getId(), seat2.getId()));
        request.setPaymentMethod("CREDIT_CARD");

        var result = mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        System.out.println("\n\n========================================");
        System.out.println("STATUS RECEBIDO: " + status);
        System.out.println("CORPO DO ERRO: " + responseBody);
        System.out.println("========================================\n\n");

        if (status != 201) {
            throw new AssertionError("O teste falhou! Esperava 201 mas veio " + status + ". Motivo: " + responseBody);
        }
    }

    @Test
    @DisplayName("Deve falhar ao tentar comprar com usuário inexistente")
    void deveFalharUsuarioInexistente() throws Exception {
        PurchaseRequestDTO request = new PurchaseRequestDTO();
        request.setUserId(9999L);
        request.setMovieSessionId(session.getId());
        request.setSeatIds(Arrays.asList(seat1.getId()));
        request.setPaymentMethod("PIX");

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}