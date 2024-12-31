package com.kaung_dev.RestaurantPOS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaung_dev.RestaurantPOS.dto.OrderDto;
import com.kaung_dev.RestaurantPOS.exception.ResourceNotFoundException;
import com.kaung_dev.RestaurantPOS.request.CreateOrderRequest;
import com.kaung_dev.RestaurantPOS.request.OrderItemRequest;
import com.kaung_dev.RestaurantPOS.response.ApiResponse;
import com.kaung_dev.RestaurantPOS.response.PusherResponse;
import com.kaung_dev.RestaurantPOS.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pusher.rest.Pusher;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final Pusher pusher;
    private final ObjectMapper objectMapper;

    @GetMapping(path = "/orders/{id}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable @Positive Long id) {
        try {
            OrderDto orderDto = orderService.getOrderById(id);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Success").data(orderDto).build(), HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(path = "/orders/active")
    public ResponseEntity<ApiResponse> getActiveOrderByTableId(@RequestParam @Positive Long tableId) {
        try {
            OrderDto activeOrder = orderService.getActiveOrderByTableId(tableId);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Active order").data(activeOrder).build(), HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping(path = "/orders")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        try {
            OrderDto order = orderService.createOrder(request);
            PusherResponse pusherResponse = PusherResponse.builder()
                    .title("Message from waiter")
                    .description("New order added.")
                    .data(order)
                    .build();
            String pusherResponseString = objectMapper.writeValueAsString(pusherResponse);
            pusher.trigger("cook", "new_order", pusherResponseString);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Order created").data(order).build(), HttpStatus.CREATED
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PatchMapping(path = "/orders/{id}")
    public ResponseEntity<ApiResponse> updateOrder(@PathVariable @Positive Long id, @RequestBody @Valid OrderItemRequest request) {
        try {
            OrderDto orderDto = orderService.updateOrder(id, request);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Order Updatd").data(orderDto).build(), HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PatchMapping(path = "/orders/{id}/complete")
    public ResponseEntity<ApiResponse> markOrderAsCompleted(@PathVariable @Positive Long id) {
        try {
            orderService.markOrderAsCompleted(id);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Order updated.").build(), HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PatchMapping(path = "/orders/{id}/cancel")
    public ResponseEntity<ApiResponse> markOrderAsCancelled(@PathVariable @Positive Long id) {
        try {
            orderService.markOrderAsCancelled(id);
            PusherResponse pusherResponse = PusherResponse.builder()
                    .title("Message from waiter")
                    .description("Order cancelled.")
                    .data(null).build();
            String pusherResponseString = objectMapper.writeValueAsString(pusherResponse);
            pusher.trigger("cook", "order_status_changed", pusherResponseString);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Order cancelled.").build(), HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PatchMapping(path = "/orders/{id}/in-progress")
    public ResponseEntity<ApiResponse> markOrderAsInProgress(@PathVariable @Positive Long id) {
        try {
            orderService.markOrderAsInProgress(id);
            PusherResponse pusherResponse = PusherResponse.builder()
                    .title("Message from cook")
                    .description("Start preparing this order.")
                    .data(null).build();
            String pusherResponseString = objectMapper.writeValueAsString(pusherResponse);
            pusher.trigger("waiter", "order_status_changed", pusherResponseString);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Order cancelled.").build(), HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(path = "/orders/in-progress")
    public ResponseEntity<ApiResponse> getActiveOrders() {
        try {
            List<OrderDto> activeOrders = orderService.getActiveOrders();
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Success").data(activeOrders).build(), HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

}
