package com.example.tableturn.reservation.reservationdto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {

    private Long id;
    private Long tableId;
    private String tableNumber;
    private String customerName;
    private int partySize;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}
