package com.kaung_dev.RestaurantPOS.request;

import com.kaung_dev.RestaurantPOS.dto.MenuItemDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemRequest {

    @NotNull
    @Valid
    private Long menuItemId;

    @NotNull
    @Positive
    private int quantity;

}
