package br.com.cinema.cinemasystem.integration;

import br.com.cinema.cinemasystem.dto.purchase.PurchaseDTO;
import br.com.cinema.cinemasystem.dto.purchase.PurchaseRequestDTO;
import br.com.cinema.cinemasystem.service.PurchaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PurchaseService purchaseService;

    @Test
    @DisplayName("Deve realizar uma compra com sucesso (Status 201)")
    void deveCriarCompraComSucesso() throws Exception {
        PurchaseRequestDTO request = new PurchaseRequestDTO();
        request.setUserId(1L);
        request.setMovieSessionId(10L);
        request.setSeatIds(Arrays.asList(55L, 56L));
        request.setPaymentMethod("CREDIT_CARD");

        PurchaseDTO responseDTO = new PurchaseDTO();
        responseDTO.setId(12345L);
        responseDTO.setUserId(1L);
        responseDTO.setMovieSessionId(10L);
        responseDTO.setTotalAmount(50.00);
        responseDTO.setPurchaseTimestamp(LocalDateTime.now());
        responseDTO.setSeatIds(Arrays.asList(55L, 56L));

        when(purchaseService.createPurchase(any(PurchaseRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(12345L))
                .andExpect(jsonPath("$.totalAmount").value(50.00));
    }

    @Test
    @DisplayName("Deve retornar Erro tratado quando o Service falhar")
    void deveFalharQuandoServiceLancarErro() throws Exception {
        PurchaseRequestDTO request = new PurchaseRequestDTO();
        request.setUserId(99L);

        when(purchaseService.createPurchase(any()))
                .thenThrow(new RuntimeException("Usuário não encontrado"));

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Usuário não encontrado"));
    }
}