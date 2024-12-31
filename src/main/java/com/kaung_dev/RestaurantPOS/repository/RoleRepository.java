package com.kaung_dev.RestaurantPOS.repository;

import com.kaung_dev.RestaurantPOS.domain.Role;
import com.kaung_dev.RestaurantPOS.domain.enums.StaffRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(StaffRole roleName);
}
