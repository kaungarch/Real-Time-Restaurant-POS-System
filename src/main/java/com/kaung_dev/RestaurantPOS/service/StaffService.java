package com.kaung_dev.RestaurantPOS.service;

import com.kaung_dev.RestaurantPOS.domain.Staff;
import com.kaung_dev.RestaurantPOS.dto.StaffDto;
import com.kaung_dev.RestaurantPOS.request.CreateStaffRequest;
import com.kaung_dev.RestaurantPOS.request.UpdateStaffRequest;

import java.util.List;

public interface StaffService {

    StaffDto createStaff(CreateStaffRequest request);

    StaffDto getStaffById(Long id);

    List<StaffDto> getAllStaffs();

    StaffDto updateStaff(Long id, UpdateStaffRequest request);

    void deleteStaff(Long id);

    StaffDto mapToStaffDto(Staff staff);

}
