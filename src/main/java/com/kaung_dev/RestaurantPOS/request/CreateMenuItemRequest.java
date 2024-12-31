package com.kaung_dev.RestaurantPOS.request;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMenuItemRequest {

    @Nonnull
    private String name;

    @Nullable
    private String description;

    @Positive
    @Min(value = 0)
    private double price;

    @Nullable
    private String imageUrl;

}
