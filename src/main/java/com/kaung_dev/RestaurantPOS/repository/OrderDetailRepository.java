package com.kaung_dev.RestaurantPOS.repository;

import com.kaung_dev.RestaurantPOS.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
