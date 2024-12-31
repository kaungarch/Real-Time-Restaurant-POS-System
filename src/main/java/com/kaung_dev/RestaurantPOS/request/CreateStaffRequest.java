package com.kaung_dev.RestaurantPOS.request;

import com.kaung_dev.RestaurantPOS.domain.enums.StaffRole;
import com.kaung_dev.RestaurantPOS.validator.ValidEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStaffRequest {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private String password;

    @NotNull
    @ValidEnum(enumClass = StaffRole.class)
    private List<StaffRole> staffRoles;

}
