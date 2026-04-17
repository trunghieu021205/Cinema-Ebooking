package com.cinemaebooking.backend.common.validation.builder;

import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.rules.LengthRule;
import com.cinemaebooking.backend.common.validation.rules.NotBlankRule;
import com.cinemaebooking.backend.common.validation.rules.NotNullRule;
import com.cinemaebooking.backend.common.validation.rules.PatternRule;

import java.util.ArrayList;
import java.util.List;
/**
 * ValidationBuilder - Fluent builder for constructing validation rule chains.
 * Responsibility:
 * - Provide a fluent API to assemble validation rules in a readable way
 * - Encapsulate rule creation logic to reduce duplication in profiles
 * - Support reusable and composable validation rule sets
 * - Improve readability of validation definitions across domains
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public class ValidationBuilder {

    private final List<ValidationRule> rules = new ArrayList<>();

    public static ValidationBuilder create() {
        return new ValidationBuilder();
    }

    public ValidationBuilder notBlank() {
        rules.add(new NotBlankRule());
        return this;
    }

    public ValidationBuilder notNull() {
        rules.add(new NotNullRule());
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

    public List<ValidationRule> build() {
        return rules;
    }
}
