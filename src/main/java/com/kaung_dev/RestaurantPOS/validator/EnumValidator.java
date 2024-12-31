package com.kaung_dev.RestaurantPOS.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumValidator implements ConstraintValidator<ValidEnum, List<? extends Enum<?>>> {
    private List<String> acceptedValues;

    @Override
    public void initialize(ValidEnum annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(List<? extends Enum<?>> values, ConstraintValidatorContext context) {
        if (values == null) {
            return true;
        }
        return values.stream()
                .map(Enum::name)
                .allMatch(acceptedValues::contains);
    }
}
