package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RoomExceptions Tests")
class RoomExceptionsTest {

    @Test
    @DisplayName("notFound() should return ROOM_NOT_FOUND")
    void notFound_shouldReturnCorrectError() {
        BaseException ex = RoomExceptions.notFound();

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ROOM_NOT_FOUND);
        assertThat(ex.getHttpStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("alreadyExists() should return ROOM_ALREADY_EXISTS")
    void alreadyExists_shouldReturnCorrectError() {
        BaseException ex = RoomExceptions.alreadyExists();

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ROOM_ALREADY_EXISTS);
        assertThat(ex.getHttpStatus()).isEqualTo(409);
    }

    @Test
    @DisplayName("invalidCapacity() with custom message")
    void invalidCapacity_withCustomMessage_shouldOverrideMessage() {
        String customMsg = "Sức chứa phòng chiếu phải lớn hơn 0";
        BaseException ex = RoomExceptions.invalidCapacity(customMsg);

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ROOM_INVALID_CAPACITY);
        assertThat(ex.getMessage()).isEqualTo(customMsg);
        assertThat(ex.getHttpStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("invalidStatus() should return ROOM_INVALID_STATUS")
    void invalidStatus_shouldReturnCorrectError() {
        BaseException ex = RoomExceptions.invalidStatus();

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ROOM_INVALID_STATUS);
        assertThat(ex.getHttpStatus()).isEqualTo(400);
    }
}