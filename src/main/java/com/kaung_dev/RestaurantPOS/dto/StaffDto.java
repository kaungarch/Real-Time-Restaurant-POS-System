package com.kaung_dev.RestaurantPOS.dto;

import com.kaung_dev.RestaurantPOS.domain.enums.StaffRole;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffDto {

    private Long id;

    private String name;

    private Set<StaffRole> roles;

}
