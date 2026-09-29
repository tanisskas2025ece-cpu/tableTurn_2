package com.example.tableturn.table.tableservice;

import com.example.tableturn.table.tableentity.Table;
import com.example.tableturn.table.tabledto.TableRequestDTO;
import com.example.tableturn.table.tabledto.TableResponseDTO;
import com.example.tableturn.table.tablerepository.TableRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class TableService {

    private final TableRepository tableRepository;

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    // CREATE TABLE
    public TableResponseDTO addTable(TableRequestDTO dto) {

        if (tableRepository.existsByTableNumberIgnoreCase(
                dto.getTableNumber())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Table number already exists"
            );
        }

        Table table = new Table();

        table.setTableNumber(dto.getTableNumber());
        table.setCapacity(dto.getCapacity());
        table.setStatus("FREE");

        Table savedTable = tableRepository.save(table);

        return convertToResponse(savedTable);
    }

    // GET ALL TABLES
    public List<TableResponseDTO> getAllTables() {

        return tableRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET AVAILABLE TABLES
    public List<TableResponseDTO> getAvailableTables() {

        return tableRepository.findByStatusIgnoreCase("FREE")
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET TABLE BY ID
    public TableResponseDTO getTableById(Long id) {

        Table table = tableRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Table not found with id: " + id
                        )
                );

        return convertToResponse(table);
    }

    // UPDATE TABLE DETAILS
    @Transactional
    public TableResponseDTO updateTable(
            Long id,
            TableRequestDTO dto) {

        Table table = tableRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Table not found with id: " + id
                        )
                );

        if (tableRepository.existsByTableNumberIgnoreCaseAndIdNot(
                dto.getTableNumber(), id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Table number already exists"
            );
        }

        table.setTableNumber(dto.getTableNumber());
        table.setCapacity(dto.getCapacity());

        Table updatedTable = tableRepository.save(table);

        return convertToResponse(updatedTable);
    }

    // UPDATE TABLE STATUS
    @Transactional
    public TableResponseDTO updateTableStatus(
            Long id,
            String status) {

        Table table = tableRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Table not found with id: " + id
                        )
                );

        String newStatus = status.toUpperCase();

        if (!newStatus.equals("FREE")
                && !newStatus.equals("RESERVED")
                && !newStatus.equals("OCCUPIED")) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid table status"
            );
        }

        table.setStatus(newStatus);

        return convertToResponse(
                tableRepository.save(table)
        );
    }

    // DELETE TABLE
    public void deleteTable(Long id) {

        Table table = tableRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Table not found with id: " + id
                        )
                );

        if (!table.getStatus().equalsIgnoreCase("FREE")) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only free tables can be deleted"
            );
        }

        tableRepository.delete(table);
    }

    // ENTITY TO RESPONSE DTO
    private TableResponseDTO convertToResponse(Table table) {

        return new TableResponseDTO(
                table.getId(),
                table.getTableNumber(),
                table.getCapacity(),
                table.getStatus()
        );
    }
}