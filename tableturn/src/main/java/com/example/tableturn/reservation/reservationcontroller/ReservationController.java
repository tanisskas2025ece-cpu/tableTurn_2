package com.example.tableturn.reservation.reservationcontroller;

import com.example.tableturn.reservation.reservationdto.ReservationRequestDTO;
import com.example.tableturn.reservation.reservationdto.ReservationResponseDTO;
import com.example.tableturn.reservation.reservationservice.ReservationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(
            ReservationService reservationService) {

        this.reservationService = reservationService;
    }

    // CREATE RESERVATION
    @PostMapping
    public ResponseEntity<ReservationResponseDTO> createReservation(
            @Valid @RequestBody ReservationRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservationService.createReservation(dto));
    }

    // GET ALL RESERVATIONS
    @GetMapping("/all")
    public ResponseEntity<List<ReservationResponseDTO>>
    getAllReservations() {

        return ResponseEntity.ok(
                reservationService.getAllReservations()
        );
    }

    // GET RESERVATION BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO>
    getReservationById(@PathVariable Long id) {

        return ResponseEntity.ok(
                reservationService.getReservationById(id)
        );
    }

    // UPDATE RESERVATION
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO>
    updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequestDTO dto) {

        return ResponseEntity.ok(
                reservationService.updateReservation(id, dto)
        );
    }

    // CANCEL RESERVATION
    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO>
    cancelReservation(@PathVariable Long id) {

        return ResponseEntity.ok(
                reservationService.cancelReservation(id)
        );
    }
}
