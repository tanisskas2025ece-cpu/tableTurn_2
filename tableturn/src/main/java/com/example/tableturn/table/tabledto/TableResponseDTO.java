package com.example.tableturn.table.tabledto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableResponseDTO {

    private Long id;
    private String tableNumber;
    private int capacity;
    private String status;
}