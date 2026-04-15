package com.cinemaebooking.backend.room.infrastructure.mapper;

import com.cinemaebooking.backend.infrastructure.persistence.mapper.BaseMapper;
import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room.infrastructure.persistence.entity.RoomJpaEntity;

public interface RoomMapper extends BaseMapper<Room, RoomJpaEntity> {
}