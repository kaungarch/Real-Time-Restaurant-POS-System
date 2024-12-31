package com.kaung_dev.RestaurantPOS.service;

import com.kaung_dev.RestaurantPOS.domain.Table;
import com.kaung_dev.RestaurantPOS.request.UpdateTableRequest;

import java.util.List;

public interface TableService {

    Table createTable(int number);

    Table updateTable(Long id, UpdateTableRequest request);

    void deleteTable(Long id);

    Table getTableById(Long id);

    List<Table> getAllTables();

    List<Table> getAvailableTables();

    void markTableAsOccupied(Long id);

    void markTableAsAvailable(Long id);

    void markTableAsReserved(Long id);

}
