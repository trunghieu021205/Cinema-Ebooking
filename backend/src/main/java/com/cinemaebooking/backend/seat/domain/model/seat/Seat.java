package com.cinemaebooking.backend.seat.domain.model.seat;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.seat.domain.enums.SeatStatus;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class Seat extends BaseEntity<SeatId> {

    private String rowLabel;
    private Integer columnNumber;
    private SeatStatus status;
    private Long seatTypeId;
    private Long roomId;

    // ================== BUSINESS METHODS ==================

    public void update(String rowLabel, Integer columnNumber, Long seatTypeId, SeatStatus status) {
        validate(rowLabel, columnNumber, status, seatTypeId);

        this.rowLabel = rowLabel;
        this.columnNumber = columnNumber;
        this.seatTypeId = seatTypeId;
        this.status = status;

    }

    public void changeStatus(SeatStatus status) {
        validateStatus(status);
        this.status = status;
    }

    public boolean isActive() {
        return this.status == SeatStatus.ACTIVE;
    }

    public void markInactive() {
        this.status = SeatStatus.INACTIVE;
    }

    // ================== VALIDATION ==================

    private void validate(String rowLabel, Integer columnNumber, SeatStatus status, Long seatTypeId) {

        if (rowLabel == null || rowLabel.trim().isEmpty()) {
            throw CommonExceptions.invalidInput("Row label must not be empty");
        }

        if (columnNumber == null || columnNumber <= 0) {
            throw CommonExceptions.invalidInput("Column number must be positive");
        }

        if (seatTypeId == null || seatTypeId <= 0) {
            throw CommonExceptions.invalidInput("Seat type id must be valid");
        }
        validateStatus(status);
    }

    private void validateStatus(SeatStatus status) {
        if (status == null) {
            throw CommonExceptions.invalidInput("Seat status must not be null");
        }
    }
}