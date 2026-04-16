package com.cinemaebooking.backend.room.infrastructure.mapper;

import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import com.cinemaebooking.backend.room.infrastructure.persistence.entity.RoomJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RoomMapperImpl implements RoomMapper {

    @Override
    public Room toDomain(RoomJpaEntity entity) {
        if (entity == null) return null;

        return Room.builder()
                .id(entity.getId() != null ? new RoomId(entity.getId()) : null)
                .name(entity.getName())
                .totalSeats(entity.getTotalSeats())
                .roomType(entity.getRoomType())
                .status(entity.getStatus())
                .cinemaId(entity.getCinema() != null ? entity.getCinema().getId() : null)
                .build();
    }

    @Override
    public RoomJpaEntity toEntity(Room room) {
        if (room == null) return null;

        return RoomJpaEntity.builder()
                .id(room.getId() != null ? room.getId().getValue() : null)
                .name(room.getName())
                .totalSeats(room.getTotalSeats())
                .roomType(room.getRoomType())
                .status(room.getStatus())
                .build();
    }
}