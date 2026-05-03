package com.cinemaebooking.backend.seat.application.usecase.seat;

import com.cinemaebooking.backend.seat.application.dto.seat.BulkUpdateResponse;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BulkSeatUpdateHelper {

    private final SeatRepository seatRepository;

    /**
     * Xử lý cập nhật hàng loạt ghế với một action cụ thể.
     */
    public BulkUpdateResponse updateSeats(List<SeatId> seatIds, Consumer<Seat> updateAction) {
        // Tìm tất cả ghế tồn tại trong DB
        List<Seat> foundSeats = seatRepository.findAllById(seatIds);
        Set<SeatId> foundIds = foundSeats.stream()
                .map(Seat::getId)
                .collect(Collectors.toSet());

        // Xác định ghế không tồn tại
        List<BulkUpdateResponse.BulkError> errors = new ArrayList<>();
        for (SeatId id : seatIds) {
            if (!foundIds.contains(id)) {
                errors.add(new BulkUpdateResponse.BulkError(id.getValue(), "Seat not found"));
            }
        }

        // Thực hiện action cập nhật trên từng ghế hợp lệ
        foundSeats.forEach(updateAction);

        // Lưu batch
        if (!foundSeats.isEmpty()) {
            seatRepository.updateBatch(foundSeats);
        }

        return new BulkUpdateResponse(foundSeats.size(), errors);
    }
}
