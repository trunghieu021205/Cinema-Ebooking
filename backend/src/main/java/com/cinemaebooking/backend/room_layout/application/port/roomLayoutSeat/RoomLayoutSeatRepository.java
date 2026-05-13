package com.cinemaebooking.backend.room_layout.application.port.roomLayoutSeat;

import com.cinemaebooking.backend.room_layout.domain.model.roomLayoutSeat.RoomLayoutSeat;
import com.cinemaebooking.backend.room_layout.domain.valueObject.roomLayoutSeat.RoomLayoutSeatId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RoomLayoutSeatRepository {
    void save(RoomLayoutSeat roomLayoutSeat);
    Optional<RoomLayoutSeat> findById(RoomLayoutSeatId id);
    List<RoomLayoutSeat> findAllById(List<RoomLayoutSeatId> ids);
    List<RoomLayoutSeat> findByRoomLayoutId(Long roomLayoutId);
    List<RoomLayoutSeat> findByRoomLayoutIdAndCoupleGroupId(Long roomLayoutId, Long coupleGroupId);
    List<RoomLayoutSeat> findByRoomLayoutIdAndCoupleGroupIdIn(Long roomLayoutId, Collection<Long> coupleGroupIds);
    boolean existsByRoomLayoutIdAndRowAndCol(Long roomLayoutId, Integer row, Integer col);

    List<RoomLayoutSeat> findAllByIds(List<Long> ids);
}






