package com.kaung_dev.RestaurantPOS.service.impl;

import com.kaung_dev.RestaurantPOS.domain.MenuItem;
import com.kaung_dev.RestaurantPOS.dto.MenuItemDto;
import com.kaung_dev.RestaurantPOS.exception.AlreadyExistsException;
import com.kaung_dev.RestaurantPOS.exception.ResourceNotFoundException;
import com.kaung_dev.RestaurantPOS.repository.MenuItemRepository;
import com.kaung_dev.RestaurantPOS.request.CreateMenuItemRequest;
import com.kaung_dev.RestaurantPOS.request.UpdateMenuItemRequest;
import com.kaung_dev.RestaurantPOS.response.MenuItemResponse;
import com.kaung_dev.RestaurantPOS.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final ModelMapper modelMapper;

    @Override
    public MenuItemDto createMenuItem(CreateMenuItemRequest request) {
        Boolean isExist = menuItemRepository.existsByName(request.getName());
        if (isExist) throw new AlreadyExistsException("Item already existed.");
        MenuItem menuItem = mapToMenuItem(request);
        MenuItem newMenuItem = menuItemRepository.save(menuItem);
        return modelMapper.map(newMenuItem, MenuItemDto.class);
    }

    private MenuItemResponse mapToMenuItemResponse(MenuItem menuItem) {
        return MenuItemResponse.builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .imageUrl(menuItem.getImageUrl())
                .build();
    }

    private MenuItem mapToMenuItem(CreateMenuItemRequest request) {
        return MenuItem.builder()
                .id(null)
                .name(request.getName())
                .description(request.getDescription().trim().length() > 0 ? request.getDescription() : null)
                .price(request.getPrice())
                .imageUrl(request.getImageUrl().trim().length() > 0 ? request.getImageUrl() : null)
                .build();
    }

    @Override
    public MenuItemDto getMenuItemById(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item not found."));

        return modelMapper.map(menuItem, MenuItemDto.class);
    }

    @Override
    public List<MenuItemDto> getAllMenuItems() {
        List<MenuItem> menuItems = menuItemRepository.findAll();
        if (menuItems.isEmpty()) return new ArrayList<>();
        return menuItems.stream()
                .map(menuItem -> modelMapper.map(menuItem, MenuItemDto.class))
                .toList();
    }

    @Override
    public MenuItemDto updateMenuItem(Long id, UpdateMenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item not" + " found."));
        Optional.ofNullable(request.getName()).ifPresent(menuItem::setName);
        Optional.ofNullable(request.getPrice()).ifPresent(menuItem::setPrice);
        Optional.ofNullable(request.getDescription()).ifPresent(menuItem::setDescription);
        Optional.ofNullable(request.getImageUrl()).ifPresent(menuItem::setImageUrl);
        MenuItem saved = menuItemRepository.save(menuItem);
        return modelMapper.map(saved, MenuItemDto.class);
    }

    @Override
    public void deleteMenuItem(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item " +
                "not found."));
        menuItemRepository.delete(menuItem);
    }
}
