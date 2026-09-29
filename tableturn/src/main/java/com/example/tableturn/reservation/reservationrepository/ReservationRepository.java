package com.example.tableturn.reservation.reservationrepository;

import com.example.tableturn.reservation.reservationentity.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    @Query("""
        SELECT COUNT(r) > 0
        FROM Reservation r
        WHERE r.table.id = :tableId
        AND r.status <> 'CANCELLED'
        AND r.startTime < :endTime
        AND r.endTime > :startTime
        """)
    boolean existsOverlappingReservation(
            @Param("tableId") Long tableId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("""
        SELECT COUNT(r) > 0
        FROM Reservation r
        WHERE r.table.id = :tableId
        AND r.id <> :reservationId
        AND r.status <> 'CANCELLED'
        AND r.startTime < :endTime
        AND r.endTime > :startTime
        """)
    boolean existsOverlappingReservationForUpdate(
            @Param("tableId") Long tableId,
            @Param("reservationId") Long reservationId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    List<Reservation> findByStatus(String status);
}
