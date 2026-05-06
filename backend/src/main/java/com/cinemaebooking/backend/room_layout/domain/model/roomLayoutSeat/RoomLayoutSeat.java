package com.cinemaebooking.backend.room_layout.domain.model.roomLayoutSeat;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.room_layout.domain.enums.SeatStatus;
import com.cinemaebooking.backend.room_layout.domain.valueObject.roomLayoutSeat.RoomLayoutSeatId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class RoomLayoutSeat extends BaseEntity<RoomLayoutSeatId> {

    private Integer rowIndex;   // 0-based, sync với Room.numberOfRows
    private Integer colIndex;   // 0-based, sync với Room.numberOfCols
    private String label;       // "A1", "B3" — generated 1 lần, readonly sau đó
    private SeatStatus status;
    @Setter
    private Long seatTypeId;
    private Long roomLayoutId;
    private Long coupleGroupId;
    // ================== BUSINESS METHODS ==================

    public void update(Long seatTypeId, SeatStatus status) {
        validateStatus(status);
        this.seatTypeId = seatTypeId;
        this.status = status;
    }

    public boolean isActive() {
        return this.status == SeatStatus.ACTIVE;
    }

    public void markInactive() {
        this.status = SeatStatus.INACTIVE;
    }

    public  void markActive() { this.status = SeatStatus.ACTIVE; }

    private void validateStatus(SeatStatus status) {
        if (status == null) {
            throw CommonExceptions.invalidInput("status", ErrorCategory.REQUIRED,"Seat status must not be null");
        }
    }
}