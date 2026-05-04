package com.cinemaebooking.backend.showtime.application.usecase.showtime;

import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import com.cinemaebooking.backend.showtime.application.dto.showtime.ShowtimeResponse;
import com.cinemaebooking.backend.showtime.application.mapper.ShowtimeResponseMapper;
import com.cinemaebooking.backend.showtime.application.port.ShowtimeRepository;
import com.cinemaebooking.backend.showtime.domain.model.Showtime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
@Service
@RequiredArgsConstructor
public class GetShowtimeUsecase {

    private final ShowtimeRepository repository;
    private final RoomRepository roomRepository;
    private final ShowtimeResponseMapper mapper;

    public Page<ShowtimeResponse> execute(
            Long cinemaId,
            Long movieId,
            LocalDate date,
            Pageable pageable
    ) {

        Page<Showtime> page = repository.search(
                cinemaId,
                movieId,
                date,
                pageable
        );

        return page.map(showtime -> {
            Long roomId = showtime.getRoomId();
            Long cId = roomRepository.getCinemaIdByRoomId(RoomId.of(roomId));
            return mapper.toResponse(showtime, cId);
        });
    }
}
