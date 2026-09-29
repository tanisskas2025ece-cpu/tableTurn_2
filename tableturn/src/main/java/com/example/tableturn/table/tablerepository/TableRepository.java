package com.example.tableturn.table.tablerepository;

import com.example.tableturn.table.tableentity.Table;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableRepository
        extends JpaRepository<Table, Long> {

    boolean existsByTableNumberIgnoreCase(
            String tableNumber
    );

    boolean existsByTableNumberIgnoreCaseAndIdNot(
            String tableNumber,
            Long id
    );

    List<Table> findByStatusIgnoreCase(String status);
}