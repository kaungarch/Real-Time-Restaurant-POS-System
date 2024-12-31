package com.kaung_dev.RestaurantPOS.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItemDto {

    private Long id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;

}
