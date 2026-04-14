package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.room.application.port.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteRoomUseCase {

    private final RoomRepository roomRepository;

    @Transactional
    public void execute(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new IllegalArgumentException("Room not found with id: " + id);
        }
        roomRepository.deleteById(id);
    }
}