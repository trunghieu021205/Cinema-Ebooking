package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CommonExceptions Tests")
class CommonExceptionsTest {

    @Test
    @DisplayName("resourceNotFound() should return correct error code and message")
    void resourceNotFound_shouldReturnCorrectError() {
        BaseException ex = CommonExceptions.resourceNotFound();

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
        assertThat(ex.getHttpStatus()).isEqualTo(404);
        assertThat(ex.getMessage()).isEqualTo("Resource not found");
    }

    @Test
    @DisplayName("resourceNotFound(String) should override default message")
    void resourceNotFound_withCustomMessage_shouldOverrideMessage() {
        String customMsg = "Không tìm thấy lịch chiếu yêu cầu";
        BaseException ex = CommonExceptions.resourceNotFound(customMsg);

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND);
        assertThat(ex.getMessage()).isEqualTo(customMsg);
        assertThat(ex.getHttpStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("invalidInput() should return INVALID_INPUT error")
    void invalidInput_shouldReturnCorrectError() {
        String customMsg = "Dữ liệu đầu vào không hợp lệ";
        BaseException ex = CommonExceptions.invalidInput(customMsg);

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.INVALID_INPUT);
        assertThat(ex.getMessage()).isEqualTo(customMsg);
        assertThat(ex.getHttpStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("unauthorized() should return 401 status")
    void unauthorized_shouldReturnUnauthorizedError() {
        BaseException ex = CommonExceptions.unauthorized();

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.UNAUTHORIZED);
        assertThat(ex.getHttpStatus()).isEqualTo(401);
    }

    @Test
    @DisplayName("concurrencyConflict() should return correct error")
    void concurrencyConflict_shouldReturnCorrectError() {
        BaseException ex = CommonExceptions.concurrencyConflict();

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.CONCURRENCY_CONFLICT);
        assertThat(ex.getHttpStatus()).isEqualTo(409);
    }
}