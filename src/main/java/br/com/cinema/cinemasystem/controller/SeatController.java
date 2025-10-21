package br.com.cinema.cinemasystem.controller;


import br.com.cinema.cinemasystem.model.Seat;
import br.com.cinema.cinemasystem.service.SeatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;


    @PostMapping
    public ResponseEntity<Seat> createSeat(@RequestBody Seat seat){
        Seat createdSeat = seatService.createSeat(seat);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSeat);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seat> findSeatById(@PathVariable Long id){
        Seat seat = seatService.findSeatById(id);
        return ResponseEntity.ok(seat);
    }

}
