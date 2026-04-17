package com.cinemaebooking.backend.common.validation.rules;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationContext;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;

/**
 * NotBlankRule - Validation rule to ensure a string value is not blank.
 * Responsibility:
 * - Validate that input string is not null, empty, or whitespace only
 * - Throw exception when validation fails
 * - Serve as a reusable rule in validation engine
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public class NotBlankRule implements ValidationRule<String> {

    @Override
    public void validate(ValidationContext<String> context) {
        String v = context.trimmed();

        if (v == null || v.isEmpty()) {
            throw CommonExceptions.invalidInput(
                    context.fieldName() + " must not be blank"
            );
        }
    }
}
