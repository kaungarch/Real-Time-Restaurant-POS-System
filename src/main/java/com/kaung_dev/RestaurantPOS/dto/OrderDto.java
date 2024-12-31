package com.kaung_dev.RestaurantPOS.dto;

import com.kaung_dev.RestaurantPOS.domain.enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private Long id;

    private TableDto table;

    private StaffDto staff;

    private List<OrderDetailDto> orderDetailList = new ArrayList<>();

    private OrderStatus status;

    private LocalDateTime createdAt;

}
