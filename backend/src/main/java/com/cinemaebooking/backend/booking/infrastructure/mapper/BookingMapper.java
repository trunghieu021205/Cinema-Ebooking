package com.cinemaebooking.backend.booking.infrastructure.mapper;

import com.cinemaebooking.backend.booking.domain.model.Booking;
import com.cinemaebooking.backend.booking.infrastructure.persistence.entity.BookingJpaEntity;
import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;

public interface BookingMapper extends BaseMapper<Booking, BookingJpaEntity> {
    void updateEntity(Booking source, BookingJpaEntity target);
}
