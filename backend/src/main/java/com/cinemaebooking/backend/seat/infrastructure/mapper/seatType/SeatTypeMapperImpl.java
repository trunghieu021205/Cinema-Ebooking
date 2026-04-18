package com.cinemaebooking.backend.seat.infrastructure.mapper.seatType;

import com.cinemaebooking.backend.seat.domain.model.seatType.SeatType;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import com.cinemaebooking.backend.seat.infrastructure.persistence.entity.SeatTypeJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SeatTypeMapperImpl implements SeatTypeMapper{

    @Override
    public SeatTypeJpaEntity toEntity(SeatType seatType) {
        if (seatType == null) return null;

        SeatTypeJpaEntity entity = new SeatTypeJpaEntity();
        entity.setId(seatType.getId() != null ? seatType.getId().getValue() : null);
        entity.setName(seatType.getName());
        entity.setBasePrice(seatType.getBasePrice());
        return entity;
    }

    @Override
    public SeatType toDomain(SeatTypeJpaEntity entity) {
        if (entity == null) return null;
        return SeatType.builder()
                .id(new SeatTypeId(entity.getId()))
                .name(entity.getName())
                .basePrice(entity.getBasePrice())
                .build();
    }
}
