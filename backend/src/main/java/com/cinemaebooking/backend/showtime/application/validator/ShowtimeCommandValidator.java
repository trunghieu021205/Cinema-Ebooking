package com.cinemaebooking.backend.showtime.application.validator;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.ShowtimeExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import com.cinemaebooking.backend.showtime.application.dto.showtime.CreateShowtimeRequest;
import com.cinemaebooking.backend.showtime.application.dto.showtime.UpdateShowtimeRequest;
import com.cinemaebooking.backend.showtime.application.port.ShowtimeRepository;
import com.cinemaebooking.backend.showtime.domain.model.Showtime;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ShowtimeCommandValidator {

    private final ShowtimeRepository showtimeRepository;

    // ================== CREATE ==================

    public void validateCreateRequest(CreateShowtimeRequest request) {

        validateCreateInput(request);

        validateCreateFields(request);

        validateTimeLogic(request.getStartTime(), request.getEndTime());

        validateConflict(
                null,
                request.getRoomId(),
                request.getStartTime(),
                request.getEndTime()
        );
    }

    // ================== UPDATE ==================

    public void validateUpdateRequest(ShowtimeId id, UpdateShowtimeRequest request) {

        validateUpdateInput(id, request);

        Showtime existing = showtimeRepository.findById(id)
                .orElseThrow(() -> ShowtimeExceptions.notFound(id));

        validateUpdateFields(request);
        
    }

    // ================== INPUT ==================

    private void validateCreateInput(CreateShowtimeRequest request) {
        if (request == null) {
            throw CommonExceptions.invalidInput("Request must not be null");
        }
    }

    private void validateUpdateInput(ShowtimeId id, UpdateShowtimeRequest request) {
        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("Showtime id and request must not be null");
        }
    }

    // ================== FIELD VALIDATION - CREATE ==================

    private void validateCreateFields(CreateShowtimeRequest request) {

        var profile = ValidationFactory.showtime();

        ValidationEngine.of()
                .validate(request.getRoomId(), "roomId", profile.roomIdRules())
                .validate(request.getMovieId(), "movieId", profile.movieIdRules())
                .validate(request.getFormatId(), "formatId", profile.formatIdRules())
                .validate(request.getStartTime(), "startTime", profile.startTimeRules())
                .validate(request.getEndTime(), "endTime", profile.endTimeRules())
                .throwIfInvalid();
    }

    // ================== FIELD VALIDATION - UPDATE ==================

    private void validateUpdateFields(UpdateShowtimeRequest request) {

        var profile = ValidationFactory.showtime();

        var engine = ValidationEngine.of();

        engine.throwIfInvalid();
    }

    // ================== BUSINESS - TIME ==================

    private void validateTimeLogic(LocalDateTime startTime, LocalDateTime endTime) {

        if (startTime == null || endTime == null) return;

        if (!startTime.isBefore(endTime)) {
            throw ShowtimeExceptions.invalidTimeRange(startTime, endTime);
        }
    }

    // ================== BUSINESS - CONFLICT ==================

    private void validateConflict(
            ShowtimeId excludeId,
            Long roomId,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {

        if (roomId == null || startTime == null || endTime == null) return;

        boolean conflict = showtimeRepository.existsRoomConflict(
                roomId,
                startTime,
                endTime,
                excludeId
        );

        if (conflict) {
            throw ShowtimeExceptions.roomTimeConflict(roomId, startTime, endTime);
        }
    }
}