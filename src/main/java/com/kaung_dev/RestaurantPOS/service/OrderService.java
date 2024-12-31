package com.kaung_dev.RestaurantPOS.service;

import com.kaung_dev.RestaurantPOS.domain.Order;
import com.kaung_dev.RestaurantPOS.dto.OrderDto;
import com.kaung_dev.RestaurantPOS.request.CreateOrderRequest;
import com.kaung_dev.RestaurantPOS.request.OrderItemRequest;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(CreateOrderRequest request);

    OrderDto getOrderById(Long id);

    OrderDto getActiveOrderByTableId(Long tableId);

    List<OrderDto> getAllOrders();

    OrderDto updateOrder(Long id, OrderItemRequest request);

    void deleteOrder(Long id);

    void markOrderAsCompleted(Long id);

    void markOrderAsCancelled(Long id);

    void markOrderAsInProgress(Long id);

    List<OrderDto> getActiveOrders();

}
