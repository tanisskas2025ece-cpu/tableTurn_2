package com.example.tableturn.table.tabledto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableStatusDTO {

    @NotBlank(message = "Status is required")
    private String status;
}