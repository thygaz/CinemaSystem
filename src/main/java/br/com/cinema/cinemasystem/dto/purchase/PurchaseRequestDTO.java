package br.com.cinema.cinemasystem.dto.purchase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequestDTO {

    private Long userId;
    private Long movieSessionId;
    private List<Long> seatIds;
    private String paymentMethod;

}
