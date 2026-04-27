package com.cinemaebooking.backend.common.validation.engine;

import com.cinemaebooking.backend.common.exception.ErrorDetail;

import java.util.Optional;

/**
 * ValidationRule — Mỗi rule trả về Optional<ErrorDetail> thay vì throw.
 * Engine sẽ là nơi duy nhất quyết định có throw hay không.
 */
public interface ValidationRule<T> {
    Optional<ErrorDetail> validate(ValidationContext<T> context);
}
