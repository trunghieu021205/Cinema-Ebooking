package com.cinemaebooking.backend.common.validation.engine;

/**
 * ValidationContext - Immutable data holder for validation execution.
 * Responsibility:
 * - Carry input value and its metadata during validation
 * - Provide context information for ValidationRule implementations
 * @author Hieu Nguyen
 * @since 2026
 */

public record ValidationContext<T>(
        T value,
        String fieldName
) {

    public String asString() {
        return value == null ? null : value.toString();
    }

    public String trimmed() {
        String v = asString();
        return v == null ? null : v.trim();
    }

    public T getValue() {
        return value;
    }

    public String getField() {
        return fieldName;
    }
}
