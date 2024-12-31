package com.kaung_dev.RestaurantPOS.controller;

import com.kaung_dev.RestaurantPOS.domain.Table;
import com.kaung_dev.RestaurantPOS.exception.AlreadyExistsException;
import com.kaung_dev.RestaurantPOS.exception.ResourceNotFoundException;
import com.kaung_dev.RestaurantPOS.request.CreateTableRequest;
import com.kaung_dev.RestaurantPOS.request.UpdateTableRequest;
import com.kaung_dev.RestaurantPOS.response.ApiResponse;
import com.kaung_dev.RestaurantPOS.service.TableService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping(path = "/tables")
    public ResponseEntity<ApiResponse> createTable(@RequestBody @Valid CreateTableRequest request) {
        try {
            Table table = tableService.createTable(request.getNumber());
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Table created.").data(table).build(), HttpStatus.CREATED
            );
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.CONFLICT
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PatchMapping(path = "/tables/{id}")
    public ResponseEntity<ApiResponse> updateTable(@PathVariable @NotNull @Positive Long id, @RequestBody @Valid UpdateTableRequest request) {
        try {
            Table updateTable = tableService.updateTable(id, request);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Updated").data(updateTable).build(), HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(path = "/tables")
    public ResponseEntity<ApiResponse> getAllTables(HttpServletRequest request) {
        log.info("🔽cookie:::: " + request.getCookies());
        try {
            List<Table> tables = tableService.getAllTables();
            return new ResponseEntity<>(
                    ApiResponse.builder().message("All tables").data(tables).build(), HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(path = "/tables/{id}")
    public ResponseEntity<ApiResponse> getTableById(@PathVariable @NotNull @Positive Long id) {
        try {
            Table table = tableService.getTableById(id);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Success").data(table).build(), HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(path = "/tables/available")
    public ResponseEntity<ApiResponse> getAvailableTables() {
        try {
            List<Table> availableTables = tableService.getAvailableTables();
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Available tables").data(availableTables).build(), HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PatchMapping(path = "/tables/{id}/occupy")
    public ResponseEntity<ApiResponse> markAsOccupied(@PathVariable @NotNull @Positive Long id) {
        try {
            tableService.markTableAsOccupied(id);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Success").build(), HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PatchMapping(path = "/tables/{id}/reserve")
    public ResponseEntity<ApiResponse> markAsReserved(@PathVariable @NotNull @Positive Long id) {
        try {
            tableService.markTableAsReserved(id);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Success").build(), HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PatchMapping(path = "/tables/{id}/available")
    public ResponseEntity<ApiResponse> markAsAvailable(@PathVariable @NotNull @Positive Long id) {
        try {
            tableService.markTableAsAvailable(id);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Success").build(), HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

}
