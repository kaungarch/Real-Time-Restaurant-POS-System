package com.kaung_dev.RestaurantPOS.service.impl;

import com.kaung_dev.RestaurantPOS.domain.Table;
import com.kaung_dev.RestaurantPOS.domain.enums.TableStatus;
import com.kaung_dev.RestaurantPOS.exception.AlreadyExistsException;
import com.kaung_dev.RestaurantPOS.exception.ResourceNotFoundException;
import com.kaung_dev.RestaurantPOS.repository.TableRepository;
import com.kaung_dev.RestaurantPOS.request.UpdateTableRequest;
import com.kaung_dev.RestaurantPOS.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;

    @Override
    public Table createTable(int number) {
        Boolean isExist = tableRepository.existsByNumber(number);
        if (isExist) throw new AlreadyExistsException("Table number " + number + " already existed.");
        Table newTable = Table.builder()
                .id(null)
                .number(number)
                .status(TableStatus.AVAILABLE)
                .build();

        return tableRepository.save(newTable);
    }

    @Override
    public Table updateTable(Long id, UpdateTableRequest request) {
        Table table = tableRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Table not found."));
        Optional.ofNullable(request.getNumber()).ifPresent(table::setNumber);
        return tableRepository.save(table);
    }

    @Override
    public void deleteTable(Long id) {
        Table table = tableRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Table not found."));
        tableRepository.delete(table);
    }

    @Override
    public Table getTableById(Long id) {
        return tableRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Table not found."));
    }

    @Override
    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }

    @Override
    public List<Table> getAvailableTables() {
        return tableRepository.findAll().stream()
                .filter(tbl -> tbl.getStatus().equals(TableStatus.AVAILABLE))
                .toList();
    }

    @Override
    public void markTableAsOccupied(Long id) {
        Table table = tableRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Table not found."));
        table.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(table);
    }

    @Override
    public void markTableAsAvailable(Long id) {
        Table table = tableRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Table not found."));
        table.setStatus(TableStatus.AVAILABLE);
        tableRepository.save(table);
    }

    @Override
    public void markTableAsReserved(Long id) {
        Table table = tableRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Table not found."));
        table.setStatus(TableStatus.RESERVED);
        tableRepository.save(table);
    }
}
