package com.cinemaebooking.backend.common.validation.builder;

import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.rules.LengthRule;
import com.cinemaebooking.backend.common.validation.rules.NotBlankRule;
import com.cinemaebooking.backend.common.validation.rules.NotNullRule;
import com.cinemaebooking.backend.common.validation.rules.PatternRule;
import com.cinemaebooking.backend.common.validation.rules.ContainsLetterRule;

import java.util.ArrayList;
import java.util.List;

/**
 * ValidationBuilder - Fluent builder for constructing validation rule chains.
 * Responsibility:
 * - Provide a fluent API to assemble validation rules in a readable way
 * - Encapsulate rule creation logic to reduce duplication in profiles
 * - Support reusable and composable validation rule sets
 * - Improve readability of validation definitions across domains
 * Notes:
 * - Generic builder to support type-safe validation rules
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public class ValidationBuilder<T> {

    private final List<ValidationRule<T>> rules = new ArrayList<>();

    public static <T> ValidationBuilder<T> create() {
        return new ValidationBuilder<>();
    }

    public ValidationBuilder<T> notNull() {
        rules.add(new NotNullRule<>());
        return this;
    }

    public ValidationBuilder<T> custom(ValidationRule<T> rule) {
        rules.add(rule);
        return this;
    }

    public List<ValidationRule<T>> build() {
        return List.copyOf(rules);
    }
}