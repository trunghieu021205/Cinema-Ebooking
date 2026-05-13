package com.cinemaebooking.backend.room_layout.application.usecase.roomLayout;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.RoomLayoutExceptions;
import com.cinemaebooking.backend.room.domain.enums.RoomType;
import com.cinemaebooking.backend.room_layout.application.dto.roomLayoutSeat.BulkUpdateResponse;
import com.cinemaebooking.backend.room_layout.application.dto.roomLayoutSeat.SeatUpdateRequest;
import com.cinemaebooking.backend.room_layout.application.port.roomLayout.RoomLayoutRepository;
import com.cinemaebooking.backend.room_layout.application.port.roomLayoutSeat.RoomLayoutSeatRepository;
import com.cinemaebooking.backend.room_layout.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.room_layout.domain.enums.SeatStatus;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayout.RoomLayout;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayoutSeat.RoomLayoutSeat;
import com.cinemaebooking.backend.room_layout.domain.service.RoomLayoutCopyService;
import com.cinemaebooking.backend.room_layout.domain.valueObject.seatType.SeatTypeId;
import com.cinemaebooking.backend.showtime.application.port.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateRoomLayoutUseCase {

    private final RoomLayoutRepository roomLayoutRepository;
    private final RoomLayoutSeatRepository seatRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final RoomLayoutCopyService copyService;

    @Transactional
    public BulkUpdateResponse execute(Long roomId,
                                      LocalDate effectiveDate,
                                      RoomType  roomType,
                                      List<SeatUpdateRequest> updates) {
        if (roomId == null) throw CommonExceptions.invalidInput("roomId", ErrorCategory.REQUIRED,"roomId must not be null");
        if (effectiveDate == null) throw CommonExceptions.invalidInput("effectiveDate", ErrorCategory.REQUIRED,"effectiveDate must not be null");
        if (roomType == null) throw CommonExceptions.invalidInput("roomType", ErrorCategory.REQUIRED, "roomType must not be null");

        RoomLayout latestLayout = roomLayoutRepository.findLatestByRoomId(roomId)
                .orElseThrow(() -> RoomLayoutExceptions.noLayoutFound(roomId));
        Long latestLayoutId = latestLayout.getId().getValue();

        boolean roomTypeChanged = !roomType.equals(latestLayout.getRoomType());
        boolean hasSeatUpdates = updates != null && !updates.isEmpty();

        boolean isUsed = latestLayout.isUsed();

        if (isUsed) {
            if (effectiveDate.isBefore(latestLayout.getEffectiveDate()) || effectiveDate.equals(latestLayout.getEffectiveDate())) {
                throw RoomLayoutExceptions.effectiveDateTooEarly(effectiveDate, latestLayout.getEffectiveDate());
            }
            if (latestLayout.getLastUsedDate() != null) {
                if (!effectiveDate.isAfter(latestLayout.getLastUsedDate())) {
                    throw CommonExceptions.invalidInput("effectiveDate", ErrorCategory.INVALID_VALUE,
                            String.format("Effective date must be after the last used date (%s) of the current layout", latestLayout.getLastUsedDate()));
                }
            }

        }else{
            Integer currentVersion = latestLayout.getLayoutVersion();
            if (currentVersion > 1) {
                // Có version trước (version - 1)
                RoomLayout previousLayout = roomLayoutRepository.findByRoomIdAndLayoutVersion(roomId, currentVersion - 1)
                        .orElseThrow(() -> new IllegalStateException("Previous version not found but version > 1"));
                if (effectiveDate.isBefore(previousLayout.getEffectiveDate())) {
                    throw CommonExceptions.invalidInput("effectiveDate", ErrorCategory.INVALID_VALUE,
                            String.format("Effective date cannot be earlier than previous version's effective date (%s)", previousLayout.getEffectiveDate()));
                }
                if (previousLayout.getLastUsedDate() != null) {
                    if (!effectiveDate.isAfter(previousLayout.getLastUsedDate())) {
                        throw CommonExceptions.invalidInput("effectiveDate", ErrorCategory.INVALID_VALUE,
                                String.format("Effective date must be after the last used date (%s) of previous layout version (%d)",
                                        previousLayout.getLastUsedDate(), currentVersion - 1));
                    }
                }
            } else {
                // Là version đầu tiên (version 1)
                if (effectiveDate.isBefore(LocalDate.now())) {
                    throw CommonExceptions.invalidInput("effectiveDate", ErrorCategory.INVALID_VALUE,
                            "Effective date cannot be in the past for the first layout version");
                }
            }
        }

        if (!roomTypeChanged && !hasSeatUpdates) {
            throw CommonExceptions.invalidInput("updates", ErrorCategory.REQUIRED,
                    "At least one change is required: either seat updates or a different roomType");
        }

        Map<Long, RoomLayoutCopyService.SeatChange> changeMap = new HashMap<>();
        List<BulkUpdateResponse.BulkError> errors = new ArrayList<>();


        if (hasSeatUpdates){
            List<RoomLayoutSeat> allSeatsInLatestLayout = seatRepository.findByRoomLayoutId(latestLayoutId);
            Map<Long, RoomLayoutSeat> seatMap = allSeatsInLatestLayout.stream()
                    .collect(Collectors.toMap(s -> s.getId().getValue(), s -> s));

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

            Map<Long, List<RoomLayoutSeat>> coupleGroupMap = allSeatsInLatestLayout.stream()
                    .filter(s -> s.getCoupleGroupId() != null)
                    .collect(Collectors.groupingBy(RoomLayoutSeat::getCoupleGroupId));

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
        }



        if (!isUsed){
            // ========== CẬP NHẬT TRỰC TIẾP (IN-PLACE) ==========
            latestLayout.setRoomType(roomType);
            latestLayout.setEffectiveDate(effectiveDate);
            roomLayoutRepository.update(latestLayout);  // hoặc save(latestLayout) nếu là JPA

            // Cập nhật từng ghế (nếu có thay đổi)
            if (!changeMap.isEmpty()) {
                // Lấy lại danh sách ghế hiện tại (có thể dùng seatMap đã có từ trên)
                // Nhưng seatMap đã được tạo từ đầu, ta có thể dùng lại nếu đảm bảo không bị thay đổi
                // Tuy nhiên để an toàn, lấy lại từ DB hoặc dùng seatMap đã có
                List<RoomLayoutSeat> currentSeats = seatRepository.findByRoomLayoutId(latestLayoutId);
                Map<Long, RoomLayoutSeat> seatMapForUpdate = currentSeats.stream()
                        .collect(Collectors.toMap(s -> s.getId().getValue(), s -> s));

                for (Map.Entry<Long, RoomLayoutCopyService.SeatChange> entry : changeMap.entrySet()) {
                    Long seatId = entry.getKey();
                    RoomLayoutSeat seat = seatMapForUpdate.get(seatId);
                    if (seat == null) continue;
                    RoomLayoutCopyService.SeatChange change = entry.getValue();
                    if (change.newStatus() != null) {
                        if (change.newStatus() == SeatStatus.ACTIVE) seat.markActive();
                        else seat.markInactive();
                    }
                    if (change.newSeatTypeId() != null) {
                        seat.setSeatTypeId(change.newSeatTypeId());
                    }
                    seatRepository.save(seat);
                }
            }

            return new BulkUpdateResponse(changeMap.size(), Collections.emptyList());
            }else{
                RoomLayout newLayout = copyService.createNextLayout(latestLayout, effectiveDate, changeMap, roomType);
                roomLayoutRepository.create(newLayout);

                return new BulkUpdateResponse(changeMap.size(), errors);
            }
    }

}