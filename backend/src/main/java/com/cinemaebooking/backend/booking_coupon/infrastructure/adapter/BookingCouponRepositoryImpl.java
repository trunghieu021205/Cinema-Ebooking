package com.cinemaebooking.backend.booking_coupon.infrastructure.adapter;

import com.cinemaebooking.backend.booking.infrastructure.persistence.repository.BookingJpaRepository;
import com.cinemaebooking.backend.booking_coupon.application.port.BookingCouponRepository;
import com.cinemaebooking.backend.booking_coupon.domain.model.BookingCoupon;
import com.cinemaebooking.backend.booking_coupon.infrastructure.mapper.BookingCouponMapper;
import com.cinemaebooking.backend.booking_coupon.infrastructure.persistence.entity.BookingCouponJpaEntity;
import com.cinemaebooking.backend.booking_coupon.infrastructure.persistence.repository.BookingCouponJpaRepository;
import com.cinemaebooking.backend.common.exception.domain.CouponExceptions;
import com.cinemaebooking.backend.coupon.domain.valueobject.CouponId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookingCouponRepositoryImpl implements BookingCouponRepository {

    private final BookingCouponJpaRepository jpaRepository;
    private final BookingCouponMapper mapper;
    private final BookingJpaRepository bookingJpaRepository;

    @Override
    public BookingCoupon save(BookingCoupon domain) {
        BookingCouponJpaEntity entity = mapper.toEntity(domain);
        entity.setBooking(bookingJpaRepository.getReferenceById(domain.getBookingId()));
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<BookingCoupon> findByBookingId(Long bookingId) {
        return jpaRepository.findByBookingIdAndDeletedAtIsNull(bookingId)
                .map(mapper::toDomain);
    }

    @Override
    public void delete(BookingCoupon domain) {

        BookingCouponJpaEntity entity = jpaRepository.findById(domain.getId().getValue())
                .orElseThrow(() -> CouponExceptions.notFound(CouponId.of(domain.getCouponId())));

        jpaRepository.delete(entity);
    }
}
