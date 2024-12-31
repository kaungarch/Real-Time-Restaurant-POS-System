package com.kaung_dev.RestaurantPOS.request;

import com.kaung_dev.RestaurantPOS.dto.OrderDetailDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {

    @NotNull
    @Positive
    private Long tableId;

    @NotNull
    @Positive
    private Long staffId;

    @NotEmpty(message = "Order items cannot be empty.")
    @Valid
    private List<OrderItemRequest> orderItemRequestList;


}
