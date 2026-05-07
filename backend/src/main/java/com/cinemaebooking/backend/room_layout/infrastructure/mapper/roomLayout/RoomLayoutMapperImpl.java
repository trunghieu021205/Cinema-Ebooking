package com.cinemaebooking.backend.room_layout.infrastructure.mapper.roomLayout;

import com.cinemaebooking.backend.room_layout.domain.model.roomLayout.RoomLayout;
import com.cinemaebooking.backend.room_layout.domain.valueObject.roomLayout.RoomLayoutId;
import com.cinemaebooking.backend.room_layout.infrastructure.mapper.roomLayoutSeat.RoomLayoutSeatMapper;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity.RoomLayoutJpaEntity;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity.RoomLayoutSeatJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoomLayoutMapperImpl implements RoomLayoutMapper{

    private final RoomLayoutSeatMapper roomLayoutSeatMapper;

    public RoomLayoutMapperImpl(RoomLayoutSeatMapper roomLayoutSeatMapper) {
        this.roomLayoutSeatMapper = roomLayoutSeatMapper;
    }

    @Override
    public RoomLayout toDomain(RoomLayoutJpaEntity entity){
        if (entity == null) return null;

        return RoomLayout.builder()
                .id(entity.getId() != null ? new RoomLayoutId(entity.getId()) : null)
                .roomId(entity.getRoomId())
                .layoutVersion(entity.getLayoutVersion())
                .effectiveDate(entity.getEffectiveDate())
                .totalRows(entity.getTotalRows())
                .totalCols(entity.getTotalCols())
                .seats(new ArrayList<>())
                .build();
    }

    @Override
    public RoomLayout toDomainWithSeats(RoomLayoutJpaEntity entity, List<RoomLayoutSeatJpaEntity> seatEntities){
        if (entity == null) return null;

        return RoomLayout.builder()
                .id(entity.getId() != null ? new RoomLayoutId(entity.getId()) : null)
                .roomId(entity.getRoomId())
                .layoutVersion(entity.getLayoutVersion())
                .effectiveDate(entity.getEffectiveDate())
                .totalRows(entity.getTotalRows())
                .totalCols(entity.getTotalCols())
                .seats(seatEntities != null ? seatEntities.stream().map(roomLayoutSeatMapper::toDomain).toList(): new ArrayList<>())
                .build();

    }

    @Override
    public RoomLayoutJpaEntity toEntity(RoomLayout domain){
        if(domain == null) return null;

        return RoomLayoutJpaEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .roomId(domain.getRoomId())
                .layoutVersion(domain.getLayoutVersion())
                .effectiveDate(domain.getEffectiveDate())
                .totalRows(domain.getTotalRows())
                .totalCols(domain.getTotalCols())
                .build();
    }
}
