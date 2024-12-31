package com.kaung_dev.RestaurantPOS.repository;

import com.kaung_dev.RestaurantPOS.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Boolean existsByName(String name);

    Staff findByName(String username);
}
