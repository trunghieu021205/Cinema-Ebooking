package com.cinemaebooking.backend.common.validation.rules;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationContext;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;

import java.util.Optional;

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
    public Optional<ErrorDetail> validate(ValidationContext<String> context) {
        String v = context.trimmed();
        if (v == null || v.isEmpty()) {
            return Optional.of(new ErrorDetail(
                    context.fieldName(),
                    ErrorCategory.REQUIRED,
                    "không được để trống"
            ));
        }
        return Optional.empty();
    }
}