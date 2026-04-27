package com.cinemaebooking.backend.common.validation.engine;

import com.cinemaebooking.backend.common.exception.ErrorDetail;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ValidationEngine — Chạy tất cả rule, gom ErrorDetail, throw một lần duy nhất.
 * Cách dùng:
 * <pre>
 *   ValidationEngine.of()
 *       .validate("email", email, new NotBlankRule(), new PatternRule(...))
 *       .validate("name",  name,  new NotBlankRule(), new LengthRule(2, 100))
 *       .throwIfInvalid();
 * </pre>
 */
public class ValidationEngine {

    private final List<ErrorDetail> errors = new ArrayList<>();

    private ValidationEngine() {}

    public static ValidationEngine of() {
        return new ValidationEngine();
    }

    public <T> ValidationEngine validate(
            T value,
            String fieldName,
            List<ValidationRule<T>> rules
    ) {
        for (ValidationRule<T> rule : rules) {
            ValidationContext<T> context = new ValidationContext<>(value, fieldName);
            Optional<ErrorDetail> error = rule.validate(context);
            error.ifPresent(errors::add);
        }
        return this;
    }
    /**
     * Validate một field với nhiều rules — chạy HẾT tất cả rules, không dừng sớm.
     */
    @SafeVarargs
    public final <T> ValidationEngine validate(
            T value,
            String fieldName,
            ValidationRule<T>... rules
    ) {
        return validate(value,fieldName , List.of(rules));
    }

    /**
     * Throw nếu có bất kỳ lỗi nào — gom toàn bộ ErrorDetail vào một exception.
     */
    public void throwIfInvalid() {
        if (!errors.isEmpty()) {
            throw CommonExceptions.invalidInput(List.copyOf(errors));
        }
    }

    /**
     * Kiểm tra có lỗi không mà không throw — dùng khi cần custom logic.
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<ErrorDetail> getErrors() {
        return List.copyOf(errors);
    }
}