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
    private Integer totalSeats;
    private RoomType roomType;
    private RoomStatus status;
    private Long cinemaId;

    // ================== BUSINESS METHODS ==================

    public void update(String name, Integer totalSeats, RoomType roomType, RoomStatus status) {
        validate(name, totalSeats, roomType, status);

        this.name = name;
        this.totalSeats = totalSeats;
        this.roomType = roomType;
        this.status = status;
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

    private void validate(String name, Integer totalSeats, RoomType roomType, RoomStatus status) {
        if (name == null || name.trim().isEmpty()) {
            throw com.cinemaebooking.backend.common.exception.domain.CommonExceptions
                    .invalidInput("Room name must not be empty");
        }

        if (totalSeats == null || totalSeats <= 0) {
            throw com.cinemaebooking.backend.common.exception.domain.CommonExceptions
                    .invalidInput("Total seats must be positive");
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


