package com.cinemaebooking.backend.showtime.application.usecase.showtime;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.ShowtimeExceptions;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import com.cinemaebooking.backend.showtime.application.dto.showtime.ShowtimeResponse;
import com.cinemaebooking.backend.showtime.application.mapper.ShowtimeResponseMapper;
import com.cinemaebooking.backend.showtime.application.port.ShowtimeRepository;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetShowtimeDetailUsecase {

    private final ShowtimeRepository repository;
    private final ShowtimeResponseMapper mapper;
    private final RoomRepository roomRepository;

    public ShowtimeResponse execute(ShowtimeId id) {

        if (id == null) {
            throw CommonExceptions.invalidInput("Showtime id must not be null");
        }

        var showtime = repository.findById(id)
                .orElseThrow(() -> ShowtimeExceptions.notFound(id));

        Long roomId = showtime.getRoomId();
        Long cinemaId = roomRepository.getCinemaIdByRoomId(RoomId.of(roomId));
        return mapper.toResponse(showtime, cinemaId);
    }
}
