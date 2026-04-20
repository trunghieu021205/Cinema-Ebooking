package com.cinemaebooking.backend.seat.infrastructure.mapper.seatType;

import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;
import com.cinemaebooking.backend.seat.domain.model.seatType.SeatType;
import com.cinemaebooking.backend.seat.infrastructure.persistence.entity.SeatTypeJpaEntity;

public interface SeatTypeMapper extends BaseMapper<SeatType, SeatTypeJpaEntity> {
}
