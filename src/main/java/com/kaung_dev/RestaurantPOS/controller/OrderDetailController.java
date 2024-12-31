package com.kaung_dev.RestaurantPOS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaung_dev.RestaurantPOS.dto.OrderDto;
import com.kaung_dev.RestaurantPOS.exception.AlreadyExistsException;
import com.kaung_dev.RestaurantPOS.exception.ResourceNotFoundException;
import com.kaung_dev.RestaurantPOS.response.ApiResponse;
import com.kaung_dev.RestaurantPOS.response.PusherResponse;
import com.kaung_dev.RestaurantPOS.service.OrderDetailService;
import com.pusher.rest.Pusher;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;
    private final Pusher pusher;
    private final ObjectMapper objectMapper;

    @PatchMapping(path = "/orders/{orderId}/order-details/{orderDetailId}/prepare")
    public ResponseEntity<ApiResponse> markOrderDetailAsPrepared(@PathVariable @Positive Long orderId, @PathVariable @Positive Long orderDetailId) {
        try {
            OrderDto orderDto = orderDetailService.markOrderDetailAsPrepared(orderId, orderDetailId);
            PusherResponse pusherResponse = PusherResponse
                    .builder()
                    .title("Message from kitchen")
                    .description("Food prepared for table " + orderDto.getTable().getNumber())
                    .data(orderDto)
                    .build();
            String pusherResponseString = objectMapper.writeValueAsString(pusherResponse);
            pusher.trigger("waiter", "order_detail_status_changed", pusherResponseString);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Order updated").data(orderDto).build(), HttpStatus.OK
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

    @PatchMapping(path = "/orders/{orderId}/order-details/{orderDetailId}/serve")
    public ResponseEntity<ApiResponse> markOrderDetailAsServed(@PathVariable @Positive Long orderId, @PathVariable @Positive Long orderDetailId) {
        try {
            OrderDto orderDto = orderDetailService.markOrderDetailAsServed(orderId, orderDetailId);
            PusherResponse pusherResponse = PusherResponse.builder()
                    .title("Message from waiter")
                    .description("Food served to table " + orderDto.getTable().getNumber())
                    .data(orderDto)
                    .build();
            String pusherResponseString = objectMapper.writeValueAsString(pusherResponse);
            pusher.trigger("cook", "order_detail_status_changed", pusherResponseString);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Order updated").data(orderDto).build(), HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.CONFLICT
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping(path = "/orders/{orderId}/order-details/{orderDetailId}")
    public ResponseEntity<ApiResponse> deleteItemFromOrder(@PathVariable @Positive Long orderId, @PathVariable @Positive Long orderDetailId) {
        try {
            OrderDto orderDto = orderDetailService.deleteExistingItem(orderId, orderDetailId);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Item deleted from order").data(orderDto).build(), HttpStatus.OK
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

}
