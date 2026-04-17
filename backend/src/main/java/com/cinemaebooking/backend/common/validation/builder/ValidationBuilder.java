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
public class ValidationBuilder {

    private final List<ValidationRule<String>> rules = new ArrayList<>();

    public static ValidationBuilder create() {
        return new ValidationBuilder();
    }

    public ValidationBuilder notBlank() {
        rules.add(new NotBlankRule());
        return this;
    }

    public ValidationBuilder notNull() {
        rules.add(new NotNullRule<>());
        return this;
    }

    public ValidationBuilder length(int min, int max) {
        rules.add(new LengthRule(min, max));
        return this;
    }

    public ValidationBuilder pattern(String regex, String message) {
        rules.add(new PatternRule(regex, message));
        return this;
    }

    public ValidationBuilder containsLetter() {
        rules.add(new ContainsLetterRule());
        return this;
    }

    public ValidationBuilder custom(ValidationRule<String> rule) {
        rules.add(rule);
        return this;
    }

    public List<ValidationRule<String>> build() {
        return rules;
    }
}