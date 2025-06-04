package com.mate.carsharing.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {
    private List<String> acceptedValues;

    /**
     * Initializes the validator by collecting all constant names from the specified enum class.
     *
     * Populates the list of accepted values with the names of the enum constants defined in the annotation.
     */
    @Override
    public void initialize(EnumValidator annotation) {
        acceptedValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    /**
     * Checks if the provided string value matches any of the accepted enum constant names.
     *
     * @param value the string to validate
     * @param context the validation context
     * @return true if the value is non-null and matches an accepted enum constant name (case-insensitive), false otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && acceptedValues.contains(value.toUpperCase());
    }
}
