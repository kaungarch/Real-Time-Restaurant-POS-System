package com.kaung_dev.RestaurantPOS.service;

import com.kaung_dev.RestaurantPOS.domain.MenuItem;
import com.kaung_dev.RestaurantPOS.dto.MenuItemDto;
import com.kaung_dev.RestaurantPOS.request.CreateMenuItemRequest;
import com.kaung_dev.RestaurantPOS.request.UpdateMenuItemRequest;
import com.kaung_dev.RestaurantPOS.response.MenuItemResponse;

import java.util.List;

public interface MenuItemService {

    MenuItemDto createMenuItem(CreateMenuItemRequest request);

    MenuItemDto getMenuItemById(Long id);

    List<MenuItemDto> getAllMenuItems();

    MenuItemDto updateMenuItem(Long id, UpdateMenuItemRequest menuItem);

    void deleteMenuItem(Long id);

}
