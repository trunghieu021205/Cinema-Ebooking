package com.cinemaebooking.backend.common.validation.rules;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationContext;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
/**
 * PatternRule - Validation rule to check string format using regex pattern.
 * Responsibility:
 * - Validate that input string matches the given regex pattern
 * - Ignore validation if value is null
 * - Throw exception when input does not match the expected format
 * - Serve as a reusable rule for format-based validation
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public class PatternRule implements ValidationRule {

    private final String pattern;
    private final String message;

    public PatternRule(String pattern, String message) {
        this.pattern = pattern;
        this.message = message;
    }

    @Override
    public void validate(ValidationContext context) {
        String v = context.value();
        if (v == null) return;

        if (!v.trim().matches(pattern)) {
            throw CommonExceptions.invalidInput(
                    context.fieldName() + " " + message
            );
        }
    }
}