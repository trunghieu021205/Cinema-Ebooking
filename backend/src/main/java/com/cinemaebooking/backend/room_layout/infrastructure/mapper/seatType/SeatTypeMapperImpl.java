package com.cinemaebooking.backend.room_layout.infrastructure.mapper.seatType;

import com.cinemaebooking.backend.room_layout.domain.model.seatType.SeatType;
import com.cinemaebooking.backend.room_layout.domain.valueObject.seatType.SeatTypeId;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity.SeatTypeJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SeatTypeMapperImpl implements SeatTypeMapper{

    @Override
    public SeatType toDomain(SeatTypeJpaEntity entity) {
        if (entity == null) return null;
        return SeatType.builder()
                .id(new SeatTypeId(entity.getId()))
                .name(entity.getName())
                .basePrice(entity.getBasePrice())
                .build();
    }

    @Override
    public SeatTypeJpaEntity toEntity(SeatType seatType) {
        if (seatType == null) return null;

        return SeatTypeJpaEntity.builder()
                .id(seatType.getId() != null ? seatType.getId().getValue() : null)
                .name(seatType.getName())
                .basePrice(seatType.getBasePrice())
                .build();
    }
}
