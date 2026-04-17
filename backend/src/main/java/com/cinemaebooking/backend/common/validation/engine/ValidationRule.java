package com.cinemaebooking.backend.common.validation.engine;

/**
 * ValidationRule - Contract for implementing validation logic.
 * Responsibility:
 * - Define a single validation rule behavior
 * - Allow pluggable validation logic via implementations
 * @author Hieu Nguyen
 * @since 2026
 */
public interface ValidationRule<T> {
    void validate(ValidationContext<T> context);
}
