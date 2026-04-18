package com.cinemaebooking.backend.seat.infrastructure.mapper.seat;

import com.cinemaebooking.backend.infrastructure.persistence.mapper.BaseMapper;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.seat.infrastructure.persistence.entity.SeatJpaEntity;

public interface SeatMapper extends BaseMapper<Seat, SeatJpaEntity> {
}