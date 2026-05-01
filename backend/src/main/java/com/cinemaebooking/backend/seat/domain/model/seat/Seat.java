package com.cinemaebooking.backend.seat.domain.model.seat;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.seat.domain.enums.SeatStatus;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class Seat extends BaseEntity<SeatId> {

    private Integer rowIndex;   // 0-based, sync với Room.numberOfRows
    private Integer colIndex;   // 0-based, sync với Room.numberOfCols
    private String label;       // "A1", "B3" — generated 1 lần, readonly sau đó
    private SeatStatus status;
    @Setter
    private Long seatTypeId;
    private Long roomId;

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
            throw CommonExceptions.invalidInput("Seat status must not be null");
        }
    }
}