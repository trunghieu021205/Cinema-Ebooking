package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.port.RoomRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetRoomUseCase {

    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public RoomResponse getById(Long id) {
        return roomRepository.findById(id)
                .map(RoomResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getAll() {
        return roomRepository.findAll()
                .stream()
                .map(RoomResponse::from)
                .collect(Collectors.toList());
    }
}
