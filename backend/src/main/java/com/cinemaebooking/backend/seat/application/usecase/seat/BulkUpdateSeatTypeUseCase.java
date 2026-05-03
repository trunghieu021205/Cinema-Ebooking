package com.cinemaebooking.backend.seat.application.usecase.seat;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.seat.application.dto.seat.BulkUpdateResponse;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BulkUpdateSeatTypeUseCase {

    private final SeatRepository seatRepository;
    private final SeatTypeRepository seatTypeRepository;

    @Transactional
    public BulkUpdateResponse execute(List<SeatId> seatIds, SeatTypeId newTypeId) {
        // 1. Basic validation
        if (seatIds == null || seatIds.isEmpty()) {
            throw CommonExceptions.invalidInput("seatIds", ErrorCategory.REQUIRED, "seatIds must not be empty");
        }
        if (newTypeId == null) {
            throw CommonExceptions.invalidInput("seatTypeId", ErrorCategory.REQUIRED, "seatTypeId must not be null");
        }
        if (!seatTypeRepository.existsById(newTypeId)) {
            throw CommonExceptions.invalidInput("seatTypeId", ErrorCategory.INVALID_VALUE, "Loại ghế không tồn tại");
        }

        // 2. Lấy danh sách ghế tồn tại và phát hiện ghế không tồn tại
        List<Seat> foundSeats = seatRepository.findAllById(seatIds);
        Set<Long> foundIds = foundSeats.stream().map(s -> s.getId().getValue()).collect(Collectors.toSet());
        List<BulkUpdateResponse.BulkError> notFoundErrors = new ArrayList<>();
        for (SeatId id : seatIds) {
            if (!foundIds.contains(id.getValue())) {
                notFoundErrors.add(new BulkUpdateResponse.BulkError(id.getValue(), "Không tìm thấy ghế"));
            }
        }
        if (foundSeats.isEmpty()) {
            return new BulkUpdateResponse(0, notFoundErrors);
        }

        // 3. Kiểm tra tất cả ghế cùng phòng
        Long commonRoomId = foundSeats.get(0).getRoomId();
        for (Seat seat : foundSeats) {
            if (!commonRoomId.equals(seat.getRoomId())) {
                throw CommonExceptions.invalidInput("seatIds", ErrorCategory.INVALID_VALUE,
                        "Tất cả ghế được chọn phải thuộc cùng một phòng");
            }
        }

        // 4. Xử lý riêng cho ghế đôi
        if (newTypeId.getValue() == 3) {
            return processCoupleCreation(foundSeats, notFoundErrors, commonRoomId);
        } else {
            return processTypeChange(foundSeats, newTypeId.getValue(), notFoundErrors, commonRoomId);
        }
    }

    /**
     * Chuyển các ghế được chọn thành ghế đôi (typeId=3).
     * Tự động ghép cặp dựa trên coupleGroupId: mỗi cặp phải có đúng 2 ghế kề nhau.
     */
    private BulkUpdateResponse processCoupleCreation(List<Seat> requestedSeats,
                                                     List<BulkUpdateResponse.BulkError> existingErrors,
                                                     Long roomId) {
        Map<Long, List<Seat>> groupMap = requestedSeats.stream()
                .collect(Collectors.groupingBy(Seat::getCoupleGroupId));

        List<Seat> toUpdate = new ArrayList<>();
        List<BulkUpdateResponse.BulkError> errors = new ArrayList<>(existingErrors);
        Set<Long> processedSeatIds = new HashSet<>();

        for (Map.Entry<Long, List<Seat>> entry : groupMap.entrySet()) {
            Long groupId = entry.getKey();
            // Lấy tất cả ghế trong cùng nhóm và cùng phòng (gồm cả ghế không được chọn)
            List<Seat> allInGroup = seatRepository.findByCoupleGroupIdAndRoomId(groupId, roomId);
            if (allInGroup.size() != 2) {
                for (Seat seat : entry.getValue()) {
                    errors.add(new BulkUpdateResponse.BulkError(seat.getId().getValue(),
                            "Không thể tạo ghế đôi: nhóm ghế này không có đúng 2 ghế (có thể do cột cuối cùng bị lẻ)"));
                }
                continue;
            }

            Seat seatA = allInGroup.get(0);
            Seat seatB = allInGroup.get(1);
            if (!seatA.getRowIndex().equals(seatB.getRowIndex()) ||
                    Math.abs(seatA.getColIndex() - seatB.getColIndex()) != 1) {
                for (Seat seat : entry.getValue()) {
                    errors.add(new BulkUpdateResponse.BulkError(seat.getId().getValue(),
                            "Nhóm ghế đôi không hợp lệ: các ghế không nằm cạnh nhau trên cùng một hàng"));
                }
                continue;
            }

            if (!processedSeatIds.contains(seatA.getId().getValue())) {
                seatA.setSeatTypeId(3L);
                toUpdate.add(seatA);
                processedSeatIds.add(seatA.getId().getValue());
            }
            if (!processedSeatIds.contains(seatB.getId().getValue())) {
                seatB.setSeatTypeId(3L);
                toUpdate.add(seatB);
                processedSeatIds.add(seatB.getId().getValue());
            }
        }

        // Những ghế được chọn nhưng không thuộc cặp hợp lệ
        for (Seat seat : requestedSeats) {
            if (!processedSeatIds.contains(seat.getId().getValue())) {
                errors.add(new BulkUpdateResponse.BulkError(seat.getId().getValue(),
                        "Ghế không thể chuyển thành ghế đôi vì thiếu ghế bên cạnh hoặc không hợp lệ"));
            }
        }

        if (!toUpdate.isEmpty()) {
            seatRepository.updateBatch(toUpdate);
        }
        return new BulkUpdateResponse(toUpdate.size(), errors);
    }

    /**
     * Chuyển ghế sang loại khác (không phải đôi). Nếu ghế đang là đôi, tự động tìm bạn cặp và
     * cập nhật cả hai.
     */
    private BulkUpdateResponse processTypeChange(List<Seat> requestedSeats,
                                                 Long newTypeId,
                                                 List<BulkUpdateResponse.BulkError> existingErrors,
                                                 Long roomId) {
        Set<Long> coupleGroupIds = requestedSeats.stream()
                .filter(s -> s.getSeatTypeId() == 3 && s.getCoupleGroupId() != null)
                .map(Seat::getCoupleGroupId)
                .collect(Collectors.toSet());

        Set<Long> seatIdsToUpdate = requestedSeats.stream()
                .map(s -> s.getId().getValue())
                .collect(Collectors.toSet());

        if (!coupleGroupIds.isEmpty()) {
            List<Seat> coupleMembers = seatRepository.findByCoupleGroupIdInAndRoomId(coupleGroupIds, roomId);
            for (Seat member : coupleMembers) {
                seatIdsToUpdate.add(member.getId().getValue());
            }
        }

        List<SeatId> allIds = seatIdsToUpdate.stream().map(SeatId::new).collect(Collectors.toList());
        List<Seat> allToUpdate = seatRepository.findAllById(allIds);
        // Áp dụng action cập nhật loại ghế cho từng ghế
        allToUpdate.forEach(seat -> seat.setSeatTypeId(newTypeId));
        seatRepository.updateBatch(allToUpdate);

        return new BulkUpdateResponse(allToUpdate.size(), existingErrors);
    }
}