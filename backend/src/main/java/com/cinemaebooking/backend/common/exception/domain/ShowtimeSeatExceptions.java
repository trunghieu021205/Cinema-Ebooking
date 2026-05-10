package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import com.cinemaebooking.backend.showtime_seat.domain.valueobject.ShowtimeSeatId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShowtimeSeatExceptions {

    public static BaseException notFound(ShowtimeSeatId id) {
        return new BaseException(
                ErrorCode.SHOWTIME_SEAT_NOT_FOUND,
                "ShowtimeSeat not found: " + id
        );
    }

    public static BaseException islocked(ShowtimeSeatId id) {
        return new BaseException(
                ErrorCode.SHOWTIME_SEAT_ALREADY_LOCKED,
                "ShowtimeSeat is locked: " + id
        );
    }

    public static BaseException unavailable(ShowtimeSeatId id) {
        return new BaseException(
                ErrorCode.SHOWTIME_SEAT_UNAVAILABLE,
                "ShowtimeSeat is unavaulable: " + id
        );
    }

}
