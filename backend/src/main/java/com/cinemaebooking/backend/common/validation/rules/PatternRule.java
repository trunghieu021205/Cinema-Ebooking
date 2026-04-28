package com.cinemaebooking.backend.common.validation.rules;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationContext;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * PatternRule - Validation rule to validate string format using a precompiled regex pattern.
 * Responsibility:
 * - Validate that input string matches the provided Pattern
 * - Skip validation if value is null (optional field support)
 * - Use precompiled Pattern for better performance
 * - Throw exception when input does not match expected format
 * - Serve as a reusable and immutable validation rule
 * Notes:
 * - Designed as a record for immutability and thread-safety
 * - Regex is compiled once during construction to avoid repeated overhead
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public record PatternRule(Pattern pattern, String message)
        implements ValidationRule<String> {

    public PatternRule(String regex, String message) {
        this(Pattern.compile(regex), message);
    }

    @Override
    public Optional<ErrorDetail> validate(ValidationContext<String> context) {
        String v = context.trimmed();
        if (v == null) return Optional.empty();

        if (!pattern.matcher(v).matches()) {
            return Optional.of(new ErrorDetail(
                    context.fieldName(),
                    ErrorCategory.INVALID_FORMAT,
                    message
            ));
        }
        return Optional.empty();
    }
}