package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * CinemaExceptions - Domain-specific exceptions for Cinema.
 * Responsibility:
 * - Provide semantic exception methods for cinema domain
 * - Delegate to CommonExceptions for consistent error handling
 * - Improve readability in use cases & validators
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CinemaExceptions {

    public static BaseException notFound(CinemaId id) {
        return new BaseException(ErrorCode.CINEMA_NOT_FOUND,
                "Không tìm thấy rạp với id: " + id);           // debugMessage
    }

    public static BaseException duplicateName(String name) {
        return new BaseException(ErrorCode.CINEMA_ALREADY_EXISTS,
                List.of(new ErrorDetail("name", ErrorCategory.DUPLICATE,
                        "tên rạp '" + name + "' đã tồn tại")));
    }

    public static BaseException duplicateLocation(String address, String city) {
        return new BaseException(ErrorCode.CINEMA_ALREADY_EXISTS,
                List.of(new ErrorDetail("address", ErrorCategory.DUPLICATE,
                        "đã có rạp tại địa chỉ này trong thành phố " + city)));
    }
}