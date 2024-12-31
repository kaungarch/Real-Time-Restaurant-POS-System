package com.kaung_dev.RestaurantPOS.dto;

import com.kaung_dev.RestaurantPOS.domain.enums.OrderDetailStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDto {

    private Long id;

    private MenuItemDto menuItem;

    private int quantity;

    private double price;

    private OrderDetailStatus status;

}
