package com.cinemaebooking.backend.room_layout.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.room_layout.domain.valueObject.roomLayoutSeat.RoomLayoutSeatId;
import com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity.RoomLayoutSeatJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoomLayoutSeatJpaRepository extends SoftDeleteJpaRepository<RoomLayoutSeatJpaEntity> {

    List<RoomLayoutSeatJpaEntity> findByRoomLayoutId(Long roomLayoutId);

    List<RoomLayoutSeatJpaEntity> findByRoomLayoutIdAndCoupleGroupId(Long roomLayoutId, Long coupleGroupId);

    List<RoomLayoutSeatJpaEntity> findByRoomLayoutIdAndCoupleGroupIdIn(Long roomLayoutId, Collection<Long> coupleGroupIds);

    boolean existsByRoomLayoutIdAndRowIndexAndColIndex(Long roomLayoutId, Integer rowIndex, Integer colIndex);

    void deleteByRoomLayoutId(Long roomLayoutId);
}