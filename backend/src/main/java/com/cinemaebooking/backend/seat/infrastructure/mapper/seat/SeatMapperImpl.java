package com.cinemaebooking.backend.seat.infrastructure.mapper.seat;

import com.cinemaebooking.backend.room.infrastructure.persistence.entity.RoomJpaEntity;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import com.cinemaebooking.backend.seat.infrastructure.persistence.entity.SeatJpaEntity;
import com.cinemaebooking.backend.seat.infrastructure.persistence.entity.SeatTypeJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SeatMapperImpl implements SeatMapper{
    @Override
    public Seat toDomain(SeatJpaEntity entity) {
        if (entity == null) return null;

        return Seat.builder()
                .id(entity.getId() != null ? new SeatId(entity.getId()) : null)
                .rowLabel(entity.getRowLabel())
                .columnNumber(entity.getColumnNumber())
                .status(entity.getStatus())
                .seatTypeId(entity.getSeatType() != null ? entity.getSeatType().getId() : null)
                .roomId(entity.getRoom() != null ? entity.getRoom().getId() : null)
                .build();
    }
    @Override
    public SeatJpaEntity toEntity(Seat seat) {
        if (seat == null) return null;

        return SeatJpaEntity.builder()
                .id(seat.getId() != null ? seat.getId().getValue() : null)
                .rowLabel(seat.getRowLabel())
                .columnNumber(seat.getColumnNumber())
                .status(seat.getStatus())
                .seatType(
                        seat.getSeatTypeId() != null
                                ? SeatTypeJpaEntity.builder()
                                .id(seat.getSeatTypeId())
                                .build()
                                : null
                )
                .room(
                        seat.getRoomId() != null
                                ? RoomJpaEntity.builder()
                                .id(seat.getRoomId())
                                .build()
                                : null
                )
                .build();
    }
}
