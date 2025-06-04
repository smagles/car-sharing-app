package com.mate.carsharing.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    /****
     * Initializes the validator by extracting the names of the fields to compare from the FieldMatch annotation.
     *
     * @param constraintAnnotation the FieldMatch annotation containing the field names to validate
     */
    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    /**
     * Validates that the two specified fields of the given object have matching values.
     *
     * Returns {@code true} if both fields are {@code null} or if their values are equal according to {@code equals()}; returns {@code false} if the values do not match or if an error occurs during field access.
     *
     * @param value the object containing the fields to compare
     * @param context the context in which the constraint is evaluated
     * @return {@code true} if the fields match; {@code false} otherwise
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object firstValue = getFieldValue(value, firstFieldName);
            Object secondValue = getFieldValue(value, secondFieldName);
            return firstValue == null && secondValue == null
                    || firstValue != null && firstValue.equals(secondValue);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves the value of a specified field from the given object using reflection.
     *
     * @param object the object from which to retrieve the field value
     * @param fieldName the name of the field to access
     * @return the value of the specified field
     * @throws Exception if the field does not exist or cannot be accessed
     */
    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
}
