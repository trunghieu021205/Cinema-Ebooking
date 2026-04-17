package com.cinemaebooking.backend.common.validation.engine;

/**
 * ValidationContext - Immutable data holder for validation execution.
 * Responsibility:
 * - Carry input value and its metadata during validation
 * - Provide context information for ValidationRule implementations
 * @author Hieu Nguyen
 * @since 2026
 */
public record ValidationContext(
        String value,
        String fieldName
) {}
