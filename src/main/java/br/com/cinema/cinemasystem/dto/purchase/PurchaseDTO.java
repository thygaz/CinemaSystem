package br.com.cinema.cinemasystem.dto.purchase;

import br.com.cinema.cinemasystem.model.Purchase;
import br.com.cinema.cinemasystem.model.Seat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDTO {

    private Long id;
    private Long userId;
    private Long movieSessionId;
    private String movieTitle;
    private List<Long> seatIds;
    private Double totalAmount;
    private LocalDateTime purchaseTimestamp;

    public PurchaseDTO(Purchase purchase) {
        this.id = purchase.getId();
        this.userId = purchase.getUser().getId();
        this.movieSessionId = purchase.getMovieSession().getId();
        this.purchaseTimestamp = purchase.getPurchaseTimestamp();

        if (purchase.getMovieSession().getMovie() != null) {
            this.movieTitle = purchase.getMovieSession().getMovie().getTitle();
        }

        this.seatIds = purchase.getSeats().stream()
                .map(Seat::getId)
                .collect(Collectors.toList());

        this.totalAmount = purchase.getSeats().size() * purchase.getMovieSession().getTicketPrice();
    }
}