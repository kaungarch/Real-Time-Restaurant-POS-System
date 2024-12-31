package com.kaung_dev.RestaurantPOS.config;

import com.kaung_dev.RestaurantPOS.domain.Role;
import com.kaung_dev.RestaurantPOS.domain.Staff;
import com.kaung_dev.RestaurantPOS.domain.enums.StaffRole;
import com.kaung_dev.RestaurantPOS.dto.StaffDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<Set<Role>, Set<StaffRole>> roleConverter = context ->
                context.getSource() == null ? Collections.emptySet() :
                        context.getSource().stream()
                                .map(Role::getName)
                                .collect(Collectors.toSet());

        modelMapper.createTypeMap(Staff.class, StaffDto.class)
                .addMappings(mapper -> mapper.using(roleConverter)
                        .map(Staff::getRoles, StaffDto::setRoles));


        return modelMapper;
    }

}
