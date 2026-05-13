package com.cinemaebooking.backend.booking_combo.infrastructure.adapter;

import com.cinemaebooking.backend.booking.infrastructure.persistence.repository.BookingJpaRepository;
import com.cinemaebooking.backend.booking_combo.application.port.BookingComboRepository;
import com.cinemaebooking.backend.booking_combo.domain.model.BookingCombo;
import com.cinemaebooking.backend.booking_combo.infrastructure.mapper.BookingComboMapper;
import com.cinemaebooking.backend.booking_combo.infrastructure.persistence.entity.BookingComboJpaEntity;
import com.cinemaebooking.backend.booking_combo.infrastructure.persistence.repository.BookingComboJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookingComboRepositoryImpl implements BookingComboRepository {

    private final BookingComboJpaRepository jpaRepository;
    private final BookingJpaRepository bookingJpaRepository;
    private final BookingComboMapper mapper;

    @Override
    public BookingCombo save(BookingCombo domain) {
        BookingComboJpaEntity entity = mapper.toEntity(domain);
        entity.setBooking(bookingJpaRepository.getReferenceById(domain.getBookingId()));
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public List<BookingCombo> findByBookingId(Long bookingId) {
        return jpaRepository.findAllByBookingIdAndDeletedAtIsNull(bookingId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookingCombo> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }


}
