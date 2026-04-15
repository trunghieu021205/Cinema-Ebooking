package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.cinema.application.dto.CinemaResponse;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.room.application.dto.RoomResponse;
import com.cinemaebooking.backend.room.application.mapper.RoomResponseMapper;
import com.cinemaebooking.backend.room.application.port.RoomRepository;

import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetRoomUseCase {

    private final RoomRepository roomRepository;
    private final RoomResponseMapper mapper;

    public Page<RoomResponse> execute(Pageable pageable) {
        return roomRepository.findAll(pageable)
                .map(mapper::toRoomResponse);
    }

    public RoomResponse execute(RoomId id) {
        return roomRepository.findById(id)
                .map(mapper::toRoomResponse)
                .orElseThrow(() -> new RuntimeException("Room with id: " + id + " not found!"));
    }
}
