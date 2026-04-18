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

        SeatJpaEntity entity = new SeatJpaEntity();
        entity.setId(seat.getId() != null ? seat.getId().getValue() : null);
        entity.setRowLabel(seat.getRowLabel());
        entity.setColumnNumber(seat.getColumnNumber());
        entity.setStatus(seat.getStatus());

        // Reference SeatType
        if (seat.getSeatTypeId() != null) {
            SeatTypeJpaEntity seatTypeRef = new SeatTypeJpaEntity();
            seatTypeRef.setId(seat.getSeatTypeId());
            entity.setSeatType(seatTypeRef);
        }

        // Reference Room
        if (seat.getRoomId() != null) {
            RoomJpaEntity room = new RoomJpaEntity();
            room.setId(seat.getRoomId());
            entity.setRoom(room);
        }

        return entity;
    }
}
