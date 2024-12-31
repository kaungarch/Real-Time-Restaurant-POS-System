package com.kaung_dev.RestaurantPOS.service.impl;

import com.kaung_dev.RestaurantPOS.domain.*;
import com.kaung_dev.RestaurantPOS.domain.enums.OrderDetailStatus;
import com.kaung_dev.RestaurantPOS.domain.enums.OrderStatus;
import com.kaung_dev.RestaurantPOS.domain.enums.TableStatus;
import com.kaung_dev.RestaurantPOS.dto.OrderDetailDto;
import com.kaung_dev.RestaurantPOS.dto.OrderDto;
import com.kaung_dev.RestaurantPOS.dto.StaffDto;
import com.kaung_dev.RestaurantPOS.exception.AlreadyExistsException;
import com.kaung_dev.RestaurantPOS.exception.ResourceNotFoundException;
import com.kaung_dev.RestaurantPOS.repository.MenuItemRepository;
import com.kaung_dev.RestaurantPOS.repository.OrderDetailRepository;
import com.kaung_dev.RestaurantPOS.repository.OrderRepository;
import com.kaung_dev.RestaurantPOS.repository.StaffRepository;
import com.kaung_dev.RestaurantPOS.request.CreateOrderRequest;
import com.kaung_dev.RestaurantPOS.request.OrderItemRequest;
import com.kaung_dev.RestaurantPOS.service.OrderService;
import com.kaung_dev.RestaurantPOS.service.StaffService;
import com.kaung_dev.RestaurantPOS.service.TableService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TableService tableService;
    private final StaffService staffService;
    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;
    private final MenuItemRepository menuItemRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDto createOrder(CreateOrderRequest request) {

        Table table = tableService.getTableById(request.getTableId());
        table.setStatus(TableStatus.OCCUPIED);
        Staff staff = staffRepository.findById(request.getStaffId()).orElseThrow(() -> new ResourceNotFoundException("Staff not " +
                "found."));

        Order order = Order.builder()
                .id(null)
                .status(OrderStatus.PENDING)
                .orderDetailList(null)
                .table(table)
                .staff(staff)
                .createdAt(LocalDateTime.now())
                .build();

        Order saved = orderRepository.save(order);

        List<OrderDetail> orderDetailList = createOrderDetailList(request.getOrderItemRequestList(), saved);
        order.setOrderDetailList(orderDetailList);

        Order updated = orderRepository.save(saved);

        return modelMapper.map(order, OrderDto.class);
    }

    private List<OrderDetail> createOrderDetailList(List<OrderItemRequest> orderItemRequestList, Order order) {
        if (orderItemRequestList.isEmpty()) return new ArrayList<>();
        log.info("📍 new order id: " + order.getId());
        List<OrderDetail> orderDetailList = new ArrayList<>();

        for (OrderItemRequest oi : orderItemRequestList) {
            MenuItem menuItem =
                    menuItemRepository.findById(oi.getMenuItemId()).orElseThrow(() -> new ResourceNotFoundException(
                            "Menu Item not found."));
            OrderDetail orderDetail = OrderDetail.builder()
                    .id(null)
                    .order(order)
                    .menuItem(menuItem)
                    .quantity(oi.getQuantity())
                    .price(0)
                    .status(OrderDetailStatus.PENDING)
                    .build();
            orderDetail.calculatePrice();
            orderDetailList.add(orderDetail);
        }
        return orderDetailList;
    }

    private Staff getStaff(Long staffId) {
        StaffDto staffDto = staffService.getStaffById(staffId);
        Staff staff = modelMapper.map(staffDto, Staff.class);
        return staff;
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found."));
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orderList = orderRepository.findAll();

        return orderList.stream().map(order -> modelMapper.map(order, OrderDto.class)).toList();
    }

    @Transactional
    @Override
//    this method is to add new menu items to an existing order
    public OrderDto updateOrder(Long id, OrderItemRequest request) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId()).orElseThrow(() -> new ResourceNotFoundException("Menu " + "Item not found."));

        OrderDetail newOrderDetail = OrderDetail.builder()
                .id(null)
                .order(order)
                .menuItem(menuItem)
                .quantity(request.getQuantity())
                .price(0)
                .status(OrderDetailStatus.PENDING)
                .build();

        newOrderDetail.calculatePrice();
        OrderDetail savedOrderDetail = orderDetailRepository.save(newOrderDetail);
        order.getOrderDetailList().add(savedOrderDetail);

        Order updatedOrder = orderRepository.save(order);

        return modelMapper.map(updatedOrder, OrderDto.class);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found."));
        orderRepository.delete(order);
    }

    @Override
    public OrderDto getActiveOrderByTableId(Long tableId) {
        Table table = tableService.getTableById(tableId);
        List<OrderStatus> statuses = List.of(OrderStatus.PENDING, OrderStatus.IN_PROGRESS);
        Order order = orderRepository.findByTableIdAndStatusIn(tableId, statuses).orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public void markOrderAsCompleted(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found."));
        order.setStatus(OrderStatus.COMPLETED);
        order.getTable().setStatus(TableStatus.AVAILABLE);
        orderRepository.save(order);
    }

    @Override
    public void markOrderAsCancelled(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found."));
        order.setStatus(OrderStatus.CANCELLED);
        order.getTable().setStatus(TableStatus.AVAILABLE);
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getActiveOrders() {
        List<OrderStatus> inactiveStatuses = List.of(OrderStatus.COMPLETED, OrderStatus.CANCELLED);
        List<Order> activeOrders = orderRepository.findByStatusNotInOrderByCreatedAtDesc(inactiveStatuses);
        return activeOrders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void markOrderAsInProgress(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        order.setStatus(OrderStatus.IN_PROGRESS);
        Order updatedOrder = orderRepository.save(order);
    }
}
