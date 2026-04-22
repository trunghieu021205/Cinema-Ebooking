package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.RoomExceptions;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteRoomUseCase {

    private final RoomRepository roomRepository;

    public void execute(RoomId id) {

        // ================== INPUT VALIDATION ==================
        if (id == null) {
            throw CommonExceptions.invalidInput("Room id must not be null");
        }

        // ================== BUSINESS VALIDATION ==================
        if (!roomRepository.existsById(id)) {
            throw RoomExceptions.notFound(id);
        }

        roomRepository.deleteById(id);
    }
}