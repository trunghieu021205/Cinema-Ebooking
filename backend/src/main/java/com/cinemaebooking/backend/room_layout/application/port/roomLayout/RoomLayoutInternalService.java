package com.cinemaebooking.backend.room_layout.application.port.roomLayout;

import com.cinemaebooking.backend.room_layout.domain.model.roomLayoutSeat.RoomLayoutSeat;
import java.util.List;
import java.util.Map;

public interface RoomLayoutInternalService {
    // Lấy Map để tra cứu nhanh: Key là ID ghế, Value là thông tin ghế
    Map<Long, RoomLayoutSeat> getMapByIds(List<Long> layoutSeatIds);

    // Lấy Map để tra cứu tên loại ghế: Key là ID loại ghế, Value là tên (VIP, NORMAL)
    Map<Long, String> getSeatTypeNameMap(List<Long> seatTypeIds);
}
