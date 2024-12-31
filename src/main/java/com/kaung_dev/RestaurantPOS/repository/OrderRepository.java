package com.kaung_dev.RestaurantPOS.repository;

import com.kaung_dev.RestaurantPOS.domain.Order;
import com.kaung_dev.RestaurantPOS.domain.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByTableId(Long id);

    List<Order> findByStatusNotInOrderByCreatedAtDesc(List<OrderStatus> orderStatuses);

    Optional<Order> findByTableIdAndStatusIn(Long tableId, List<OrderStatus> statuses);
}
