package com.cinemaebooking.backend.room_layout.infrastructure.mapper.roomLayoutSeat;

import com.cinemaebooking.backend.room_layout.domain.model.roomLayoutSeat.RoomLayoutSeat;
import com.cinemaebooking.backend.room_layout.domain.valueObject.roomLayoutSeat.RoomLayoutSeatId;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity.RoomLayoutSeatJpaEntity;
import org.springframework.stereotype.Component;


@Component
public class RoomLayoutSeatMapperImpl implements RoomLayoutSeatMapper {

    @Override
    public RoomLayoutSeat toDomain(RoomLayoutSeatJpaEntity entity) {
        if (entity == null) return null;

        return RoomLayoutSeat.builder()
                .id(entity.getId() != null ? new RoomLayoutSeatId(entity.getId()) : null)
                .rowIndex(entity.getRowIndex())
                .colIndex(entity.getColIndex())
                .label(entity.getLabel())
                .status(entity.getStatus())
                .seatTypeId(entity.getSeatTypeId())
                .roomLayoutId(entity.getRoomLayoutId() != null ? entity.getRoomLayoutId() : null)
                .coupleGroupId(entity.getCoupleGroupId())
                .build();
    }

    @Override
    public RoomLayoutSeatJpaEntity toEntity(RoomLayoutSeat domain) {
        if (domain == null) return null;

        return RoomLayoutSeatJpaEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .rowIndex(domain.getRowIndex())
                .colIndex(domain.getColIndex())
                .label(domain.getLabel())
                .status(domain.getStatus())
                .seatTypeId(domain.getSeatTypeId())
                .coupleGroupId(domain.getCoupleGroupId())
                .build();
    }

}
