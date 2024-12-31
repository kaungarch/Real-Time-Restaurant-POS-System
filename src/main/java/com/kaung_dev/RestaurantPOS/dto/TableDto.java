package com.kaung_dev.RestaurantPOS.dto;

import com.kaung_dev.RestaurantPOS.domain.enums.TableStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableDto {

    private int id;

    private int number;

    private TableStatus status;

}
