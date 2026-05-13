package com.cinemaebooking.backend.payment.infrastructure.mapper;

import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.infrastructure.persistence.entity.CinemaJpaEntity;
import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;
import com.cinemaebooking.backend.payment.domain.model.Payment;
import com.cinemaebooking.backend.payment.infrastructure.persistence.entity.PaymentJpaEntity;

public interface PaymentMapper extends BaseMapper<Payment, PaymentJpaEntity> {
    void updateEntity(Payment domain, PaymentJpaEntity entity);
}
