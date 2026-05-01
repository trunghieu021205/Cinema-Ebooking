package com.cinemaebooking.backend.showtime_seat.domain.model;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Domain Model: ShowtimeSeat
 * - Đại diện 1 mapping giữa Showtime và Seat
 * - Không có CRUD public
 * - Không lưu trạng thái ghế (status nằm ở SeatLock / Ticket)
 * - Được auto-generate khi tạo showtime
 * - Bị hard delete khi xoá showtime
 * Domain chỉ kiểm tra dữ liệu thô, không truy database.
 */
@Getter
@SuperBuilder(toBuilder = true)
public class ShowtimeSeat {

    private final Long showtimeId;
    private final Long seatId;

    /**
     * Validate dữ liệu domain:
     * - Domain chỉ check dữ liệu nội tại, không check tồn tại trong DB
     */
    public void validate() {
        if (showtimeId == null) {
            throw CommonExceptions.invalidInput("ShowtimeId must not be null");
        }

        if (seatId == null) {
            throw CommonExceptions.invalidInput("SeatId must not be null");
        }
    }
    public static ShowtimeSeat from(Seat seat, ShowtimeId showtimeId) {

        if (seat == null) {
            throw CommonExceptions.invalidInput("Seat must not be null");
        }

        if (showtimeId == null) {
            throw CommonExceptions.invalidInput("ShowtimeId must not be null");
        }

        if (seat.getId() == null || seat.getId().getValue() == null) {
            throw CommonExceptions.invalidInput("SeatId must not be null");
        }

        ShowtimeSeat showtimeSeat = ShowtimeSeat.builder()
                .showtimeId(showtimeId.getValue())
                .seatId(seat.getId().getValue())
                .build();

        showtimeSeat.validate();

        return showtimeSeat;
    }}
