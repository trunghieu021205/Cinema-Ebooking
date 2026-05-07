package com.cinemaebooking.backend.showtime_seat.domain.model;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayoutSeat.RoomLayoutSeat;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import com.cinemaebooking.backend.showtime_seat.domain.enums.ShowtimeSeatStatus;
import com.cinemaebooking.backend.showtime_seat.domain.valueobject.ShowtimeSeatId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

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
public class ShowtimeSeat extends BaseEntity<ShowtimeSeatId> {

    private final Long showtimeId;
    private final Long roomLayoutSeatId;
    private final ShowtimeSeatStatus status;
    private final BigDecimal price;

    /**
     * Validate dữ liệu domain:
     * - Domain chỉ check dữ liệu nội tại, không check tồn tại trong DB
     */

    public static ShowtimeSeat from(RoomLayoutSeat roomLayoutSeat, ShowtimeId showtimeId, BigDecimal price) {
        if (showtimeId == null) {
            throw CommonExceptions.invalidInput("showtimeId", ErrorCategory.REQUIRED,"ShowtimeId must not be null");
        }

        if (roomLayoutSeat.getId() == null) {
            throw CommonExceptions.invalidInput("roomLayoutSeatId", ErrorCategory.REQUIRED,"RoomLayoutSeatId must not be null");
        }


        if (price == null) throw CommonExceptions.invalidInput("price",ErrorCategory.REQUIRED,"Price must not be null");
        if (price.compareTo(BigDecimal.ZERO) < 0) throw CommonExceptions.invalidInput("price",ErrorCategory.INVALID_VALUE,"Giá ghế không được âm");
        ShowtimeSeat showtimeSeat = ShowtimeSeat.builder()
                .showtimeId(showtimeId.getValue())
                .roomLayoutSeatId(roomLayoutSeat.getId().getValue())
                .status(ShowtimeSeatStatus.AVAILABLE)
                .price(price)
                .build();


        return showtimeSeat;
    }}
