package com.kaung_dev.RestaurantPOS.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMenuItemRequest {

    private String name;
    private String description;
    private double price;
    private String imageUrl;

}
