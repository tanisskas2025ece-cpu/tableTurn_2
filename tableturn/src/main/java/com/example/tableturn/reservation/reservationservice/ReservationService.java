package com.example.tableturn.reservation.reservationservice;

import com.example.tableturn.reservation.reservationentity.Reservation;
import com.example.tableturn.reservation.reservationdto.ReservationRequestDTO;
import com.example.tableturn.reservation.reservationdto.ReservationResponseDTO;
import com.example.tableturn.reservation.reservationrepository.ReservationRepository;

import com.example.tableturn.table.tableentity.Table;
import com.example.tableturn.table.tablerepository.TableRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            TableRepository tableRepository) {

        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
    }

    // CREATE RESERVATION
    @Transactional
    public ReservationResponseDTO createReservation(
            ReservationRequestDTO dto) {

        validateReservationTime(dto);

        Table table = getTable(dto.getTableId());

        validateCapacity(table, dto.getPartySize());

        if (reservationRepository.existsOverlappingReservation(
                table.getId(),
                dto.getStartTime(),
                dto.getEndTime())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Table already has a reservation during this time"
            );
        }

        Reservation reservation = new Reservation();

        reservation.setTable(table);
        reservation.setCustomerName(dto.getCustomerName());
        reservation.setPartySize(dto.getPartySize());
        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());
        reservation.setStatus("BOOKED");

        Reservation saved =
                reservationRepository.save(reservation);

        return convertToResponse(saved);
    }

    // GET ALL RESERVATIONS
    public List<ReservationResponseDTO> getAllReservations() {

        return reservationRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET RESERVATION BY ID
    public ReservationResponseDTO getReservationById(Long id) {

        Reservation reservation = getReservation(id);

        return convertToResponse(reservation);
    }

    // UPDATE RESERVATION
    @Transactional
    public ReservationResponseDTO updateReservation(
            Long id,
            ReservationRequestDTO dto) {

        validateReservationTime(dto);

        Reservation reservation = getReservation(id);

        if (reservation.getStatus().equals("CANCELLED")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cancelled reservation cannot be updated"
            );
        }

        Table table = getTable(dto.getTableId());

        validateCapacity(table, dto.getPartySize());

        if (reservationRepository
                .existsOverlappingReservationForUpdate(
                        table.getId(),
                        id,
                        dto.getStartTime(),
                        dto.getEndTime())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Table already has a reservation during this time"
            );
        }

        reservation.setTable(table);
        reservation.setCustomerName(dto.getCustomerName());
        reservation.setPartySize(dto.getPartySize());
        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());

        Reservation updated =
                reservationRepository.save(reservation);

        return convertToResponse(updated);
    }

    // CANCEL RESERVATION
    @Transactional
    public ReservationResponseDTO cancelReservation(Long id) {

        Reservation reservation = getReservation(id);

        if (reservation.getStatus().equals("CANCELLED")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Reservation is already cancelled"
            );
        }

        reservation.setStatus("CANCELLED");

        Reservation cancelled =
                reservationRepository.save(reservation);

        return convertToResponse(cancelled);
    }

    // GET TABLE
    private Table getTable(Long tableId) {

        return tableRepository.findById(tableId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Table not found with id: " + tableId
                        )
                );
    }

    // GET RESERVATION
    private Reservation getReservation(Long id) {

        return reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Reservation not found with id: " + id
                        )
                );
    }

    // VALIDATE TIME
    private void validateReservationTime(
            ReservationRequestDTO dto) {

        if (!dto.getEndTime().isAfter(dto.getStartTime())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End time must be after start time"
            );
        }
    }

    // VALIDATE TABLE CAPACITY
    private void validateCapacity(
            Table table,
            int partySize) {

        if (partySize > table.getCapacity()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Party size exceeds table capacity"
            );
        }
    }

    // ENTITY TO RESPONSE DTO
    private ReservationResponseDTO convertToResponse(
            Reservation reservation) {

        return new ReservationResponseDTO(
                reservation.getId(),
                reservation.getTable().getId(),
                reservation.getTable().getTableNumber(),
                reservation.getCustomerName(),
                reservation.getPartySize(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getStatus()
        );
    }
}