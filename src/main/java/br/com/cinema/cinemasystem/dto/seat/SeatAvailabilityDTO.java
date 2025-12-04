package br.com.cinema.cinemasystem.dto.seat;

import br.com.cinema.cinemasystem.model.Seat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatAvailabilityDTO {

    private Long id;
    private Character rowIdentifier; // A, B, C...
    private Integer seatNumber;      // 1, 2, 3...
    private SeatStatus status;       // AVAILABLE, LOCKED, SOLD

    // Construtor auxiliar para converter Entity -> DTO
    public SeatAvailabilityDTO(Seat seat) {
        this.id = seat.getId();
        this.rowIdentifier = seat.getRowIdentifier();
        this.seatNumber = seat.getSeatNumber();
        this.status = seat.getStatus();
    }
}