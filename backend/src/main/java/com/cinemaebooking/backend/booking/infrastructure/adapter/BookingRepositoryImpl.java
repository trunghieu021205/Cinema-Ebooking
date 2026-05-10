package com.cinemaebooking.backend.booking.infrastructure.adapter;

import com.cinemaebooking.backend.booking.application.port.BookingRepository;
import com.cinemaebooking.backend.booking.domain.enums.BookingStatus;
import com.cinemaebooking.backend.booking.domain.model.Booking;
import com.cinemaebooking.backend.booking.infrastructure.mapper.BookingMapper;
import com.cinemaebooking.backend.booking.infrastructure.persistence.entity.BookingJpaEntity;
import com.cinemaebooking.backend.booking.infrastructure.persistence.repository.BookingJpaRepository;
import com.cinemaebooking.backend.user.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {

    private final BookingJpaRepository jpaRepository;
    private final BookingMapper mapper;
    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public Booking save(Booking booking) {
        BookingJpaEntity entity = mapper.toEntity(booking);
        entity.setUser(userJpaRepository.getReferenceById(booking.getUserId()));
        BookingJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Booking> findById(Long id) {
        return jpaRepository.findWithDetailsById(id)
                .filter(entity -> !entity.isDeleted())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Booking> findByBookingCode(String bookingCode) {
        return jpaRepository.findByBookingCodeAndDeletedFalse(bookingCode)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Booking> findByUserId(Long userId, BookingStatus status, Pageable pageable) {
        Page<BookingJpaEntity> jpaPage = (status == null)
                ? jpaRepository.findByUserIdAndDeletedFalse(userId, pageable)
                : jpaRepository.findByUserIdAndStatusAndDeletedFalse(userId, status, pageable);

        return jpaPage.map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Booking> findWithDetailsById(Long id) {
        return jpaRepository.findWithDetailsById(id)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> findAllExpired(LocalDateTime now, BookingStatus status) {
        return jpaRepository.findAllByExpiredAtBeforeAndStatusAndDeletedFalse(now, status)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByBookingCode(String bookingCode) {
        return jpaRepository.existsByBookingCodeAndDeletedFalse(bookingCode);
    }
}