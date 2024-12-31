package com.kaung_dev.RestaurantPOS.request;

import com.kaung_dev.RestaurantPOS.domain.enums.StaffRole;
import com.kaung_dev.RestaurantPOS.validator.ValidEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStaffRequest {

    @NotNull
    @Min(2)
    private String name;

}
