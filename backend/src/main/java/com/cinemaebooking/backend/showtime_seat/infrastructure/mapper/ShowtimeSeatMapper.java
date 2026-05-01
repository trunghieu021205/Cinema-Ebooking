package com.cinemaebooking.backend.showtime_seat.infrastructure.mapper;

import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;
import com.cinemaebooking.backend.showtime_seat.domain.model.ShowtimeSeat;
import com.cinemaebooking.backend.showtime_seat.infrastructure.persistence.entity.ShowtimeSeatJpaEntity;

public interface ShowtimeSeatMapper extends BaseMapper<ShowtimeSeat, ShowtimeSeatJpaEntity> {
}
