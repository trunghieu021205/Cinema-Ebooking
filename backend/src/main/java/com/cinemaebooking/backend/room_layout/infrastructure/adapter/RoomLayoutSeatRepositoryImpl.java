package com.cinemaebooking.backend.room_layout.infrastructure.adapter;

import com.cinemaebooking.backend.room_layout.application.port.roomLayoutSeat.RoomLayoutSeatRepository;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayoutSeat.RoomLayoutSeat;
import com.cinemaebooking.backend.room_layout.domain.valueObject.roomLayoutSeat.RoomLayoutSeatId;
import com.cinemaebooking.backend.room_layout.infrastructure.mapper.roomLayoutSeat.RoomLayoutSeatMapper;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity.RoomLayoutSeatJpaEntity;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.repository.RoomLayoutSeatJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoomLayoutSeatRepositoryImpl implements RoomLayoutSeatRepository {

    private final RoomLayoutSeatJpaRepository seatJpaRepository;
    private final RoomLayoutSeatMapper mapper;

    @Override
    public void save(RoomLayoutSeat roomLayoutSeat) {
        var oldEntity = seatJpaRepository.findByIdOrThrow(roomLayoutSeat.getId().getValue());

        oldEntity.setStatus(roomLayoutSeat.getStatus());
        oldEntity.setSeatTypeId(roomLayoutSeat.getSeatTypeId());
    }

    @Override
    public Optional<RoomLayoutSeat> findById(RoomLayoutSeatId id) {
        return seatJpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<RoomLayoutSeat> findAllById(List<RoomLayoutSeatId> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        List<Long> idValues = ids.stream()
                .map(RoomLayoutSeatId::getValue)
                .collect(Collectors.toList());
        return seatJpaRepository.findAllById(idValues)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomLayoutSeat> findByRoomLayoutId(Long roomLayoutId) {
        if (roomLayoutId == null) {
            return List.of();
        }
        return seatJpaRepository.findByRoomLayoutId(roomLayoutId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomLayoutSeat> findByRoomLayoutIdAndCoupleGroupId(Long roomLayoutId, Long coupleGroupId) {
        if (roomLayoutId == null || coupleGroupId == null) {
            return List.of();
        }
        return seatJpaRepository.findByRoomLayoutIdAndCoupleGroupId(roomLayoutId, coupleGroupId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomLayoutSeat> findByRoomLayoutIdAndCoupleGroupIdIn(Long roomLayoutId, Collection<Long> coupleGroupIds) {
        if (roomLayoutId == null || coupleGroupIds == null || coupleGroupIds.isEmpty()) {
            return List.of();
        }
        return seatJpaRepository.findByRoomLayoutIdAndCoupleGroupIdIn(roomLayoutId, coupleGroupIds)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByRoomLayoutIdAndRowAndCol(Long roomLayoutId, Integer row, Integer col) {
        if (roomLayoutId == null || row == null || col == null) {
            return false;
        }
        return seatJpaRepository.existsByRoomLayoutIdAndRowIndexAndColIndex(roomLayoutId, row, col);
    }

    @Override
    public List<RoomLayoutSeat> findAllByIds(List<Long> ids) {
        // findAllById là hàm có sẵn của JpaRepository
        return seatJpaRepository.findAllById(ids).stream()
                .map(mapper::toDomain) // Chuyển từ Entity sang Model Domain
                .collect(Collectors.toList());
    }
}