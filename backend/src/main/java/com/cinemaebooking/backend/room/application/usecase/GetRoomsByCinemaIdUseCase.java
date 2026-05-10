package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.helper.RoomResponsePageHelper;

import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * GetRoomsByCinemaIdUseCase - Handles retrieving rooms by cinemaId (with pagination).
 */
@Service
@RequiredArgsConstructor
public class GetRoomsByCinemaIdUseCase {

    private final RoomRepository roomRepository;
    private final RoomResponsePageHelper roomResponsePageHelper;

    public Page<RoomResponse> execute(Long cinemaId, Pageable pageable) {

        // ================== VALIDATION ==================
        if (cinemaId == null) {
            throw CommonExceptions.invalidInput("cinemaId", ErrorCategory.REQUIRED,"Cinema id must not be null");
        }

        if (pageable == null) {
            throw CommonExceptions.invalidInput("pageable", ErrorCategory.REQUIRED,"Pageable must not be null");
        }

        // ================== BUSINESS ==================
        Page<Room> roomPage = roomRepository.findByCinemaId(cinemaId, pageable);
        LocalDate date = LocalDate.now();
        return roomResponsePageHelper.mapToResponsePage(roomPage,date);
    }
}