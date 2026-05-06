package com.cinemaebooking.backend.room_layout.infrastructure.mapper.seatType;

import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;
import com.cinemaebooking.backend.room_layout.domain.model.seatType.SeatType;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity.SeatTypeJpaEntity;

public interface SeatTypeMapper extends BaseMapper<SeatType, SeatTypeJpaEntity> {
}
