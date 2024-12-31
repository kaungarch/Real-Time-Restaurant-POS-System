package com.kaung_dev.RestaurantPOS.service.impl;

import com.kaung_dev.RestaurantPOS.domain.Role;
import com.kaung_dev.RestaurantPOS.domain.Staff;
import com.kaung_dev.RestaurantPOS.domain.enums.StaffRole;
import com.kaung_dev.RestaurantPOS.dto.StaffDto;
import com.kaung_dev.RestaurantPOS.exception.AlreadyExistsException;
import com.kaung_dev.RestaurantPOS.exception.ResourceNotFoundException;
import com.kaung_dev.RestaurantPOS.repository.RoleRepository;
import com.kaung_dev.RestaurantPOS.repository.StaffRepository;
import com.kaung_dev.RestaurantPOS.request.CreateStaffRequest;
import com.kaung_dev.RestaurantPOS.request.UpdateStaffRequest;
import com.kaung_dev.RestaurantPOS.service.StaffService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public StaffDto createStaff(CreateStaffRequest request) {
        Boolean isExist = staffRepository.existsByName(request.getName());
        if (isExist) throw new AlreadyExistsException("Staff with name " + request.getName() + " already existed.");

        Set<Role> roles = getStaffRoles(request.getStaffRoles());

        Staff staff = Staff.builder()
                .id(null)
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();
        Staff newStaff = staffRepository.save(staff);

        return mapToStaffDto(newStaff);
    }

    @Override
    public StaffDto mapToStaffDto(Staff staff) {
        Set<StaffRole> roles = staff.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());

        return StaffDto.builder()
                .id(staff.getId())
                .name(staff.getName())
                .roles(roles)
                .build();

    }

    private Set<Role> getStaffRoles(@NotNull List<StaffRole> staffRoles) {

        Set<Role> roles = new HashSet<>();

        for (StaffRole roleName : staffRoles) {
            Role existingRole = roleRepository.findByName(roleName);
            if (existingRole != null)
                roles.add(existingRole);
            else throw new ResourceNotFoundException("Role with name = " + roleName + " not found.");
        }

        return roles;

    }

    @Override
    public StaffDto getStaffById(Long id) {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff not found."));
        return modelMapper.map(staff, StaffDto.class);
    }

    @Override
    public List<StaffDto> getAllStaffs() {
        List<Staff> staffList = staffRepository.findAll();
        if (staffList.isEmpty()) return new ArrayList<>();

        return staffList.stream()
                .map(staff -> mapToStaffDto(staff))
                .toList();
    }

    @Override
    public void deleteStaff(Long id) {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff not found."));
        staffRepository.delete(staff);
    }

    @Override
    public StaffDto updateStaff(Long id, UpdateStaffRequest request) {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff not found."));

        Optional.ofNullable(request.getName()).ifPresent(staff::setName);

        Staff saved = staffRepository.save(staff);
        return mapToStaffDto(saved);
    }
}
