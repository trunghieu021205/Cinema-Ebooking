package com.cinemaebooking.backend.showtime.application.usecase.showtime;

import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.showtime.application.dto.showtime.CreateShowtimeRequest;
import com.cinemaebooking.backend.showtime.application.dto.showtime.ShowtimeResponse;
import com.cinemaebooking.backend.showtime.application.mapper.ShowtimeResponseMapper;
import com.cinemaebooking.backend.showtime.application.port.ShowtimeRepository;
import com.cinemaebooking.backend.showtime.application.validator.ShowtimeCommandValidator;
import com.cinemaebooking.backend.showtime.domain.enums.ShowtimeStatus;
import com.cinemaebooking.backend.showtime.domain.model.Showtime;
import com.cinemaebooking.backend.showtime_seat.application.port.ShowtimeSeatRepository;
import com.cinemaebooking.backend.showtime_seat.domain.model.ShowtimeSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateShowtimeUsecase {

    private final ShowtimeRepository showtimeRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;
    private final ShowtimeResponseMapper mapper;
    private final ShowtimeCommandValidator validator;

    @Transactional
    public ShowtimeResponse execute(CreateShowtimeRequest request) {

        // ================== VALIDATION ==================
        validator.validateCreateRequest(request);

        // ================== BUILD DOMAIN ==================
        Showtime showtime = buildShowtime(request);
        showtime.validateForCreate();

        // ================== SAVE SHOWTIME ==================
        Showtime saved = showtimeRepository.create(showtime);

        // ================== GENERATE SHOWTIME SEAT ==================
        generateShowtimeSeats(saved);

        Long cinemaId = roomRepository.getCinemaIdByRoomId(RoomId.of(request.getRoomId()));
        // ================== RESPONSE ==================
        return mapper.toResponse(saved, cinemaId);
    }

    private Showtime buildShowtime(CreateShowtimeRequest request) {
        return Showtime.builder()
                .movieId(request.getMovieId())
                .roomId(request.getRoomId())
                .formatId(request.getFormatId())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .audioLanguage(request.getAudioLanguage())
                .subtitleLanguage(request.getSubtitleLanguage())
                .status(ShowtimeStatus.ACTIVE)
                .build();
    }

    private void generateShowtimeSeats(Showtime showtime) {

        List<Seat> seats = seatRepository.findByRoomId(showtime.getRoomId());

        if (seats.isEmpty()) {
            throw new IllegalStateException("Room has no seats to generate showtime seats");
        }

        List<ShowtimeSeat> showtimeSeats = seats.stream()
                .map(seat -> ShowtimeSeat.from(seat, showtime.getId()))
                .toList();

        showtimeSeatRepository.saveAll(showtimeSeats);
    }
}