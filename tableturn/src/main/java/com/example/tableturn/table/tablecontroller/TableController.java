package com.example.tableturn.table.tablecontroller;

import com.example.tableturn.table.tabledto.TableRequestDTO;
import com.example.tableturn.table.tabledto.TableResponseDTO;
import com.example.tableturn.table.tabledto.TableStatusDTO;
import com.example.tableturn.table.tableservice.TableService;

import jakarta.validation.Valid;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    // CREATE TABLE
    @PostMapping
    public ResponseEntity<TableResponseDTO> addTable(
            @Valid @RequestBody TableRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tableService.addTable(dto));
    }

    // GET ALL TABLES
    @GetMapping("/all")
    public ResponseEntity<List<TableResponseDTO>> getAllTables() {

        return ResponseEntity.ok(
                tableService.getAllTables()
        );
    }

    // GET AVAILABLE TABLES
    @GetMapping("/available")
    public ResponseEntity<List<TableResponseDTO>>
    getAvailableTables() {

        return ResponseEntity.ok(
                tableService.getAvailableTables()
        );
    }

    // GET TABLE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<TableResponseDTO> getTableById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                tableService.getTableById(id)
        );
    }

    // UPDATE TABLE DETAILS
    @PutMapping("/{id}")
    public ResponseEntity<TableResponseDTO> updateTable(
            @PathVariable Long id,
            @Valid @RequestBody TableRequestDTO dto) {

        return ResponseEntity.ok(
                tableService.updateTable(id, dto)
        );
    }

    // UPDATE TABLE STATUS
    @PutMapping("/{id}/status")
    public ResponseEntity<TableResponseDTO> updateTableStatus(
            @PathVariable Long id,
            @Valid @RequestBody TableStatusDTO dto) {

        return ResponseEntity.ok(
                tableService.updateTableStatus(
                        id,
                        dto.getStatus()
                )
        );
    }

    // DELETE TABLE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(
            @PathVariable Long id) {

        tableService.deleteTable(id);

        return ResponseEntity.noContent().build();
    }
}
