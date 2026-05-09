package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.helper.RoomResponsePageHelper;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.room.domain.model.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * GetRoomListUseCase - Handles retrieving paginated room list.
 */
@Service
@RequiredArgsConstructor
public class GetRoomListUseCase {

    private final RoomRepository roomRepository;
    private final RoomResponsePageHelper roomResponsePageHelper;

    public Page<RoomResponse> execute(Pageable pageable) {

        // ================== VALIDATION ==================
        if (pageable == null) {
            throw CommonExceptions.invalidInput("pageable", ErrorCategory.REQUIRED,"Pageable must not be null");
        }

        // ================== BUSINESS ==================
        Page<Room> roomPage = roomRepository.findAll(pageable);
        LocalDate date = LocalDate.now();
        return roomResponsePageHelper.mapToResponsePage(roomPage,date);
    }
}