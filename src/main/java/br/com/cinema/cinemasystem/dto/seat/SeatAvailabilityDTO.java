package br.com.cinema.cinemasystem.dto.seat;

public class SeatAvailabilityDTO {

    private Long seatId;
    private String seatRow;// Ex: "A"
    private Integer seatNumber; // Ex: 10
    private SeatStatus status; // O status calculado
}
