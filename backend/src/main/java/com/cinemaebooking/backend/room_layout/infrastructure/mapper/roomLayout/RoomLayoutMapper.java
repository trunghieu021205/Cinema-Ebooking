package com.cinemaebooking.backend.room_layout.infrastructure.mapper.roomLayout;

import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayout.RoomLayout;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity.RoomLayoutJpaEntity;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity.RoomLayoutSeatJpaEntity;

import java.util.List;

public interface RoomLayoutMapper extends BaseMapper<RoomLayout, RoomLayoutJpaEntity> {
    RoomLayout toDomainWithSeats(RoomLayoutJpaEntity entity, List<RoomLayoutSeatJpaEntity> seatEntities);
}
