package com.cinemaebooking.backend.common.validation.engine;

import java.util.List;
/**
 * ValidationEngine - Core engine to execute validation rules.
 * Responsibility:
 * - Orchestrate validation process for a given value
 * - Aggregate and execute multiple ValidationRule implementations
 * - Provide a unified entry point for validation flow
 * - Stateless and reusable across all domains
 * @author Hieu Nguyen
 * @since 2026
 */
public class ValidationEngine {

    public static <T> void validate(
            T value,
            String fieldName,
            List<ValidationRule<T>> rules
    ) {
        if (rules == null || rules.isEmpty()) return;

        ValidationContext<T> context = new ValidationContext<>(value, fieldName);

        for (ValidationRule<T> rule : rules) {
            rule.validate(context);
        }
    }
}
