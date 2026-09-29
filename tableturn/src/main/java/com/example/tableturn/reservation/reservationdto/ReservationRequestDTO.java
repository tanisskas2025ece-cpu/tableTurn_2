package com.example.tableturn.reservation.reservationdto;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDTO {

    @NotNull(message = "Table ID is required")
    @Positive(message = "Table ID must be positive")
    private Long tableId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Positive(message = "Party size must be greater than zero")
    private int partySize;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;
}
