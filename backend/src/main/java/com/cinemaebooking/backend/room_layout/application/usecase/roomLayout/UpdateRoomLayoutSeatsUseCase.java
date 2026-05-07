package com.cinemaebooking.backend.room_layout.application.usecase.roomLayout;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.RoomLayoutExceptions;
import com.cinemaebooking.backend.room_layout.application.dto.roomLayoutSeat.BulkUpdateResponse;
import com.cinemaebooking.backend.room_layout.application.dto.roomLayoutSeat.SeatUpdateRequest;
import com.cinemaebooking.backend.room_layout.application.port.roomLayout.RoomLayoutRepository;
import com.cinemaebooking.backend.room_layout.application.port.roomLayoutSeat.RoomLayoutSeatRepository;
import com.cinemaebooking.backend.room_layout.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayout.RoomLayout;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayoutSeat.RoomLayoutSeat;
import com.cinemaebooking.backend.room_layout.domain.service.RoomLayoutCopyService;
import com.cinemaebooking.backend.room_layout.domain.valueObject.seatType.SeatTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateRoomLayoutSeatsUseCase {

    private final RoomLayoutRepository roomLayoutRepository;
    private final RoomLayoutSeatRepository seatRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final RoomLayoutCopyService copyService;

    @Transactional
    public BulkUpdateResponse execute(Long roomId,
                                      LocalDate effectiveDate,
                                      List<SeatUpdateRequest> updates) {
        if (roomId == null) throw CommonExceptions.invalidInput("roomId", ErrorCategory.REQUIRED,"roomId must not be null");
        if (effectiveDate == null) throw CommonExceptions.invalidInput("effectiveDate", ErrorCategory.REQUIRED,"effectiveDate must not be null");
        if (updates == null || updates.isEmpty()) {
            throw CommonExceptions.invalidInput("updates", ErrorCategory.REQUIRED,"updates must not be empty");
        }

        RoomLayout currentLayout = roomLayoutRepository.findCurrentByRoomIdAndDate(roomId, LocalDate.now())
                .orElseThrow(() -> RoomLayoutExceptions.noCurrentLayout(roomId, LocalDate.now()));
        Long currentLayoutId = currentLayout.getId().getValue();

        RoomLayout latestLayout = roomLayoutRepository.findLatestByRoomId(roomId)
                .orElseThrow(() -> RoomLayoutExceptions.noLayoutFound(roomId));

        if (effectiveDate.isBefore(latestLayout.getEffectiveDate())) {
            throw RoomLayoutExceptions.effectiveDateTooEarly(effectiveDate, latestLayout.getEffectiveDate());
        }

        // 1. Lấy tất cả ghế liên quan trong layout hiện tại để xử lý
        List<RoomLayoutSeat> allSeatsInCurrentLayout = seatRepository.findByRoomLayoutId(currentLayoutId);
        Map<Long, RoomLayoutSeat> seatMap = allSeatsInCurrentLayout.stream()
                .collect(Collectors.toMap(s -> s.getId().getValue(), s -> s));

        Map<Long, RoomLayoutCopyService.SeatChange> changeMap = new HashMap<>();
        List<BulkUpdateResponse.BulkError> errors = new ArrayList<>();

        // 2. Validate và ghi nhận thay đổi ban đầu
        for (SeatUpdateRequest req : updates) {
            Long seatId = req.seatId();
            RoomLayoutSeat seat = seatMap.get(seatId);
            if (seat == null) {
                errors.add(new BulkUpdateResponse.BulkError(seatId, "Seat not found or not in current layout"));
                continue;
            }
            Long newTypeId = req.newSeatTypeId();
            if (newTypeId != null && !seatTypeRepository.existsById(SeatTypeId.of(newTypeId))) {
                errors.add(new BulkUpdateResponse.BulkError(seatId, "Invalid seat type id"));
                continue;
            }
            changeMap.put(seatId, new RoomLayoutCopyService.SeatChange(req.newStatus(), newTypeId));
        }

        if (changeMap.isEmpty()) {
            return new BulkUpdateResponse(0, errors);
        }

        // 3. Xử lý ghế đôi (couple)
        // 3a. Map coupleGroupId -> list seats
        Map<Long, List<RoomLayoutSeat>> coupleGroupMap = allSeatsInCurrentLayout.stream()
                .filter(s -> s.getCoupleGroupId() != null)
                .collect(Collectors.groupingBy(RoomLayoutSeat::getCoupleGroupId));

        // 3b. Trường hợp chuyển thành ghế đôi (newTypeId == 3)
        Set<Long> additionalSeatIds = new HashSet<>();
        for (Map.Entry<Long, RoomLayoutCopyService.SeatChange> entry : new HashMap<>(changeMap).entrySet()) {
            Long seatId = entry.getKey();
            RoomLayoutCopyService.SeatChange change = entry.getValue();
            if (change.newSeatTypeId() != null && change.newSeatTypeId() == 3L) {
                RoomLayoutSeat seat = seatMap.get(seatId);
                if (seat != null && seat.getCoupleGroupId() != null) {
                    Long groupId = seat.getCoupleGroupId();
                    List<RoomLayoutSeat> groupSeats = coupleGroupMap.get(groupId);
                    if (groupSeats != null && groupSeats.size() == 2) {
                        for (RoomLayoutSeat cs : groupSeats) {
                            if (!cs.getId().getValue().equals(seatId)) {
                                additionalSeatIds.add(cs.getId().getValue());
                            }
                        }
                    } else {
                        errors.add(new BulkUpdateResponse.BulkError(seatId, "Invalid couple group: does not have exactly 2 seats"));
                    }
                }
            }
        }
        for (Long id : additionalSeatIds) {
            changeMap.putIfAbsent(id, new RoomLayoutCopyService.SeatChange(null, 3L));
        }

        // 3c. Trường hợp ghế đang là ghế đôi (currentType == 3) và chuyển sang loại khác
        Set<Long> groupIdsToProcess = new HashSet<>();
        for (Map.Entry<Long, RoomLayoutCopyService.SeatChange> entry : changeMap.entrySet()) {
            Long seatId = entry.getKey();
            RoomLayoutSeat seat = seatMap.get(seatId);
            if (seat != null && seat.getSeatTypeId() == 3L && seat.getCoupleGroupId() != null) {
                Long newTypeId = entry.getValue().newSeatTypeId();
                if (newTypeId != null && newTypeId != 3L) {
                    groupIdsToProcess.add(seat.getCoupleGroupId());
                }
            }
        }
        if (!groupIdsToProcess.isEmpty()) {
            Map<Long, Long> groupTargetType = new HashMap<>();
            for (Map.Entry<Long, RoomLayoutCopyService.SeatChange> entry : changeMap.entrySet()) {
                Long seatId = entry.getKey();
                RoomLayoutSeat seat = seatMap.get(seatId);
                if (seat != null && seat.getCoupleGroupId() != null && groupIdsToProcess.contains(seat.getCoupleGroupId())) {
                    Long newTypeId = entry.getValue().newSeatTypeId();
                    if (newTypeId != null && newTypeId != 3L) {
                        groupTargetType.putIfAbsent(seat.getCoupleGroupId(), newTypeId);
                    }
                }
            }
            for (Long groupId : groupIdsToProcess) {
                Long targetTypeId = groupTargetType.get(groupId);
                if (targetTypeId == null) continue;
                List<RoomLayoutSeat> groupSeats = coupleGroupMap.get(groupId);
                if (groupSeats != null) {
                    for (RoomLayoutSeat seat : groupSeats) {
                        if (!changeMap.containsKey(seat.getId().getValue())) {
                            changeMap.put(seat.getId().getValue(), new RoomLayoutCopyService.SeatChange(null, targetTypeId));
                        }
                    }
                }
            }
        }

        if (changeMap.isEmpty()) {
            return new BulkUpdateResponse(0, errors);
        }

        // 4. Tạo layout mới
        RoomLayout newLayout = copyService.createNextLayout(currentLayout, effectiveDate, changeMap);
        roomLayoutRepository.create(newLayout);

        return new BulkUpdateResponse(changeMap.size(), errors);
    }

}