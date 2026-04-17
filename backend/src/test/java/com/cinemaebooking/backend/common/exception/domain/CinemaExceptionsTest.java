package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CinemaExceptions Tests")
class CinemaExceptionsTest {

    @Test
    @DisplayName("notFound() should return CINEMA_NOT_FOUND with default message")
    void notFound_shouldReturnCorrectError() {

        BaseException ex = CinemaExceptions.notFound();

        assertThat(ex.getErrorCode())
                .isEqualTo(ErrorCode.CINEMA_NOT_FOUND);

        assertThat(ex.getMessage())
                .isEqualTo(ErrorCode.CINEMA_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("alreadyExists() should return CINEMA_ALREADY_EXISTS")
    void alreadyExists_shouldReturnCorrectError() {

        BaseException ex = CinemaExceptions.alreadyExists();

        assertThat(ex.getErrorCode())
                .isEqualTo(ErrorCode.CINEMA_ALREADY_EXISTS);

        assertThat(ex.getMessage())
                .isEqualTo(ErrorCode.CINEMA_ALREADY_EXISTS.getMessage());
    }

    @Test
    @DisplayName("invalidStatus() should override default message when custom provided")
    void invalidStatus_withCustomMessage_shouldOverrideMessage() {

        String customMsg = "Trạng thái rạp không hợp lệ";

        BaseException ex = CinemaExceptions.invalidStatus(customMsg);

        assertThat(ex.getErrorCode())
                .isEqualTo(ErrorCode.CINEMA_INVALID_STATUS);

        assertThat(ex.getMessage())
                .isEqualTo(customMsg);
    }
}