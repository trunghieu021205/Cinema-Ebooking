package com.cinemaebooking.backend.showtime.application.usecase.showtime;

import com.cinemaebooking.backend.common.exception.domain.ShowtimeExceptions;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import com.cinemaebooking.backend.showtime.application.dto.showtime.ShowtimeResponse;
import com.cinemaebooking.backend.showtime.application.dto.showtime.UpdateShowtimeRequest;
import com.cinemaebooking.backend.showtime.application.mapper.ShowtimeResponseMapper;
import com.cinemaebooking.backend.showtime.application.port.ShowtimeRepository;
import com.cinemaebooking.backend.showtime.application.validator.ShowtimeCommandValidator;
import com.cinemaebooking.backend.showtime.domain.model.Showtime;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateShowtimeUsecase {

    private final ShowtimeRepository repository;
    private final ShowtimeResponseMapper mapper;
    private final ShowtimeCommandValidator validator;
    private final RoomRepository roomRepository;

    @Transactional
    public ShowtimeResponse execute(ShowtimeId id, UpdateShowtimeRequest request) {
        validator.validateUpdateRequest(id, request);

        Showtime showtime = repository.findById(id)
                .orElseThrow(() -> ShowtimeExceptions.notFound(id));

        applyUpdate(showtime, request);

        Showtime saved = repository.update(showtime);

        Long cinemaId = roomRepository.getCinemaIdByRoomId(RoomId.of(showtime.getRoomId()));
        return mapper.toResponse(saved, cinemaId);
    }

    private void applyUpdate(Showtime showtime, UpdateShowtimeRequest request) {

        if (request.getStartTime() != null || request.getEndTime() != null) {

            var newStart = request.getStartTime() != null
                    ? request.getStartTime()
                    : showtime.getStartTime();

            var newEnd = request.getEndTime() != null
                    ? request.getEndTime()
                    : showtime.getEndTime();

            showtime.update(newStart, newEnd);
        }

        if (request.getAudioLanguage() != null || request.getSubtitleLanguage() != null) {
            showtime.updateLanguage(
                    request.getAudioLanguage(),
                    request.getSubtitleLanguage()
            );
        }
    }
}