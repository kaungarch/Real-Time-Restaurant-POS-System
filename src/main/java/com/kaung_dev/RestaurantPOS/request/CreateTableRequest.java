package com.kaung_dev.RestaurantPOS.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTableRequest {

    @NotNull
    @Positive
    @Min(value = 1)
    private int number;

}
