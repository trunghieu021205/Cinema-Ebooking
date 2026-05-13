package com.cinemaebooking.backend.room_layout.infrastructure.adapter;

import com.cinemaebooking.backend.room_layout.application.port.roomLayout.RoomLayoutInternalService;
import com.cinemaebooking.backend.room_layout.application.port.roomLayoutSeat.RoomLayoutSeatRepository;
import com.cinemaebooking.backend.room_layout.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayoutSeat.RoomLayoutSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomLayoutInternalServiceImpl implements RoomLayoutInternalService {

    private final RoomLayoutSeatRepository seatRepository;
    private final SeatTypeRepository seatTypeRepository;

    @Override
    public Map<Long, RoomLayoutSeat> getMapByIds(List<Long> layoutSeatIds) {
        // Lấy tất cả ghế theo danh sách ID
        List<RoomLayoutSeat> seats = seatRepository.findAllByIds(layoutSeatIds);

        // Chuyển thành Map để module Showtime tra cứu O(1)
        return seats.stream().collect(Collectors.toMap(
                seat -> seat.getId().getValue(),
                seat -> seat
        ));
    }

    @Override
    public Map<Long, String> getSeatTypeNameMap(List<Long> seatTypeIds) {
        // Giả sử bạn có hàm findAllByIds trong SeatTypeRepository
        return seatTypeRepository.findAllByIds(seatTypeIds).stream()
                .collect(Collectors.toMap(
                        type -> type.getId().getValue(),
                        type -> type.getName() // Lấy chuỗi "VIP", "NORMAL"...
                ));
    }
}
