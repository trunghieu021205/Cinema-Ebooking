package com.cinemaebooking.backend.room.domain.model;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.room.domain.enums.RoomStatus;
import com.cinemaebooking.backend.room.domain.enums.RoomType;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class Room extends BaseEntity<RoomId> {

    private String name;
    private RoomType roomType;
    private Integer numberOfRows;
    private Integer numberOfCols;
    private Integer totalSeats;
    private RoomStatus status;
    private Long cinemaId;

    // ================== BUSINESS METHODS ==================

    public void update(String name, RoomType roomType, RoomStatus status) {
        validate(name, roomType, status);
        this.name = name;
        this.roomType = roomType;
        this.status = status;
    }

    public int computeTotalSeats() {
        if(this.numberOfRows == null || this.numberOfCols == null) return 0;
        return this.numberOfRows * this.numberOfCols;
    }

    public void changeStatus(RoomStatus status) {
        validateStatus(status);
        this.status = status;
    }

    public void activate() {
        this.status = RoomStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = RoomStatus.INACTIVE;
    }

    public boolean isActive() {
        return this.status == RoomStatus.ACTIVE;
    }

    // ================== VALIDATION ==================

    private void validate(String name, RoomType roomType, RoomStatus status) {
        if (name == null || name.trim().isEmpty()) {
            throw com.cinemaebooking.backend.common.exception.domain.CommonExceptions
                    .invalidInput("Room name must not be empty");
        }

        if (roomType == null) {
            throw com.cinemaebooking.backend.common.exception.domain.CommonExceptions
                    .invalidInput("Room type must not be null");
        }

        validateStatus(status);
    }

    private void validateStatus(RoomStatus status) {
        if (status == null) {
            throw com.cinemaebooking.backend.common.exception.domain.CommonExceptions
                    .invalidInput("Room status must not be null");
        }
    }
}


