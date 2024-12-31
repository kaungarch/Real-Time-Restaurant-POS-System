package com.kaung_dev.RestaurantPOS.controller;

import com.kaung_dev.RestaurantPOS.dto.StaffDto;
import com.kaung_dev.RestaurantPOS.exception.AlreadyExistsException;
import com.kaung_dev.RestaurantPOS.exception.ResourceNotFoundException;
import com.kaung_dev.RestaurantPOS.request.CreateStaffRequest;
import com.kaung_dev.RestaurantPOS.request.UpdateStaffRequest;
import com.kaung_dev.RestaurantPOS.response.ApiResponse;
import com.kaung_dev.RestaurantPOS.service.StaffService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PostMapping(path = "/staffs")
    public ResponseEntity<ApiResponse> createStaff(@RequestBody @Valid CreateStaffRequest request) {
        try {
            StaffDto staff = staffService.createStaff(request);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Staff created").data(staff).build(), HttpStatus.CREATED
            );
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.CONFLICT
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(path = "/staffs/{id}")
    public ResponseEntity<ApiResponse> getStaffById(@PathVariable Long id) {
        try {
            StaffDto staff = staffService.getStaffById(id);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("success").data(staff).build(), HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(path = "/staffs")
    public ResponseEntity<ApiResponse> getStaffById() {
        try {
            List<StaffDto> allStaffs = staffService.getAllStaffs();
            return new ResponseEntity<>(
                    ApiResponse.builder().message("success").data(allStaffs).build(), HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping(path = "/staffs/{id}")
    public ResponseEntity<ApiResponse> deleteStaffById(@PathVariable Long id) {
        try {
            staffService.deleteStaff(id);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Staff deleted.").build(), HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PatchMapping(path = "/staffs/{id}")
    public ResponseEntity<ApiResponse> updateStaff(@PathVariable Long id, @RequestBody @Valid UpdateStaffRequest request) {
        try {
            StaffDto staffDto = staffService.updateStaff(id, request);
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Staff updated").data(staffDto).build(), HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

}
