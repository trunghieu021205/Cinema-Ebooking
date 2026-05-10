package com.cinemaebooking.backend.booking_combo.infrastructure.mapper;

import com.cinemaebooking.backend.booking_combo.domain.model.BookingCombo;
import com.cinemaebooking.backend.booking_combo.domain.valueObject.BookingComboId;
import com.cinemaebooking.backend.booking_combo.infrastructure.persistence.entity.BookingComboJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class BookingComboMapperImpl implements BookingComboMapper {

    @Override
    public BookingComboJpaEntity toEntity(BookingCombo domain) {
        return BookingComboJpaEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .comboId(domain.getComboId())
                .comboName(domain.getComboName())
                .unitPrice(domain.getUnitPrice())
                .quantity(domain.getQuantity())
                .totalPrice(domain.getTotalPrice())
                .build();
    }

    @Override
    public BookingCombo toDomain(BookingComboJpaEntity entity) {
        return BookingCombo.builder()
                .id(BookingComboId.of(entity.getId()))
                .bookingId(entity.getBooking().getId())
                .comboId(entity.getComboId())
                .comboName(entity.getComboName())
                .unitPrice(entity.getUnitPrice())
                .quantity(entity.getQuantity())
                .totalPrice(entity.getTotalPrice())
                .build();
    }
}