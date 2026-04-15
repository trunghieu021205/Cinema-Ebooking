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

        RoomJpaEntity entity = new RoomJpaEntity();
        entity.setId(room.getId() != null ? room.getId().getValue() : null);
        entity.setName(room.getName());
        entity.setTotalSeats(room.getTotalSeats());
        entity.setRoomType(room.getRoomType());
        entity.setStatus(room.getStatus());
        return entity; // Không set cinema ở đây
    }
}