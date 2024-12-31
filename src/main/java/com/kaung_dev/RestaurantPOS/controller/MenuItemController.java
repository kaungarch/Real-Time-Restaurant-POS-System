package com.kaung_dev.RestaurantPOS.controller;

import com.kaung_dev.RestaurantPOS.domain.MenuItem;
import com.kaung_dev.RestaurantPOS.dto.MenuItemDto;
import com.kaung_dev.RestaurantPOS.exception.AlreadyExistsException;
import com.kaung_dev.RestaurantPOS.exception.ResourceNotFoundException;
import com.kaung_dev.RestaurantPOS.request.CreateMenuItemRequest;
import com.kaung_dev.RestaurantPOS.request.UpdateMenuItemRequest;
import com.kaung_dev.RestaurantPOS.response.ApiResponse;
import com.kaung_dev.RestaurantPOS.response.MenuItemResponse;
import com.kaung_dev.RestaurantPOS.service.MenuItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping(path = "/menu-items")
    public ResponseEntity<ApiResponse> createMenuItem(@RequestBody @Valid CreateMenuItemRequest request) {

        try {
            MenuItemDto menuItem = menuItemService.createMenuItem(request);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Item created").data(menuItem).build(), HttpStatus.CREATED
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

    @GetMapping(path = "/menu-items/{id}")
    public ResponseEntity<ApiResponse> getMenuItemById(@PathVariable @NotNull @Positive Long id) {
        try {
            MenuItemDto menuItemDto = menuItemService.getMenuItemById(id);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("success").data(menuItemDto).build(), HttpStatus.OK
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

    @GetMapping(path = "/menu-items")
    public ResponseEntity<ApiResponse> getAllMenuItems() {
        try {
            List<MenuItemDto> menuItemDtoList = menuItemService.getAllMenuItems();
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Menu Items").data(menuItemDtoList).build(), HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @PatchMapping(path = "/menu-items/{id}")
    public ResponseEntity<ApiResponse> updateMenuItem(@PathVariable @NotNull @Positive Long id,
                                                      @RequestBody @Valid UpdateMenuItemRequest request) {
        try {
            MenuItemDto updatedMenuItemDto = menuItemService.updateMenuItem(id, request);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Updated").data(updatedMenuItemDto).build(), HttpStatus.OK
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

    @DeleteMapping(path = "/menu-items/{id}")
    public ResponseEntity<ApiResponse> deleteMenuItem(@PathVariable @NotNull @Positive Long id) {
        try {
            menuItemService.deleteMenuItem(id);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Deleted").build(), HttpStatus.OK
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
