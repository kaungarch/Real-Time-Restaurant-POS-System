package com.kaung_dev.RestaurantPOS.repository;

import com.kaung_dev.RestaurantPOS.domain.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    Boolean existsByName(String menuItemName);
}
