package com.example.tableturn.table.tabledto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableRequestDTO {

    @NotBlank(message = "Table number is required")
    private String tableNumber;

    @Positive(message = "Capacity must be greater than zero")
    private int capacity;
}
