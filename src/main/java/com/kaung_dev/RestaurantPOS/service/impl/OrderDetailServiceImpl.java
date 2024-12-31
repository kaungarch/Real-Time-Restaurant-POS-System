package com.kaung_dev.RestaurantPOS.service.impl;

import com.kaung_dev.RestaurantPOS.domain.Order;
import com.kaung_dev.RestaurantPOS.domain.OrderDetail;
import com.kaung_dev.RestaurantPOS.domain.enums.OrderDetailStatus;
import com.kaung_dev.RestaurantPOS.dto.OrderDto;
import com.kaung_dev.RestaurantPOS.exception.AlreadyExistsException;
import com.kaung_dev.RestaurantPOS.exception.ResourceNotFoundException;
import com.kaung_dev.RestaurantPOS.repository.OrderDetailRepository;
import com.kaung_dev.RestaurantPOS.repository.OrderRepository;
import com.kaung_dev.RestaurantPOS.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDto markOrderDetailAsPrepared(Long orderId, Long orderDetailId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found."));
        for (OrderDetail orderDetail : order.getOrderDetailList()) {
            if (orderDetail.getId().equals(orderDetailId))
                orderDetail.setStatus(OrderDetailStatus.PREPARED);
        }
        Order updated = orderRepository.save(order);
        return modelMapper.map(updated, OrderDto.class);
    }

    @Override
    public OrderDto markOrderDetailAsServed(Long orderId, Long orderDetailId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found."));
        OrderDetail foundOrderDetail = order.getOrderDetailList().stream()
                .filter(orderDetail -> orderDetail.getId().equals(orderDetailId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Order detail not found."));

        if (foundOrderDetail.getStatus().equals(OrderDetailStatus.SERVED))
            throw new AlreadyExistsException("Order detail has been already updated.");
        foundOrderDetail.setStatus(OrderDetailStatus.SERVED);

        Order updatedOrder = orderRepository.save(order);
        return modelMapper.map(updatedOrder, OrderDto.class);
    }

    @Override
    public OrderDto deleteExistingItem(Long orderId, Long orderDetailId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found."));
        Optional<OrderDetail> foundOrderDetail = order.getOrderDetailList().stream()
                .filter(orderDetail -> orderDetail.getId().equals(orderDetailId))
                .findFirst();

        foundOrderDetail.ifPresent(orderDetail -> {
            orderDetail.setOrder(null);
            order.getOrderDetailList().remove(orderDetail);
            orderDetailRepository.delete(orderDetail);
        });

        return modelMapper.map(order, OrderDto.class);

    }

}
