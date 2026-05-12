package com.cinemaebooking.backend.showtime.application.validator;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.RoomExceptions;
import com.cinemaebooking.backend.common.exception.domain.ShowtimeExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import com.cinemaebooking.backend.movie.application.port.MovieRepository;
import com.cinemaebooking.backend.movie.domain.model.Movie;
import com.cinemaebooking.backend.movie.domain.valueobject.MovieId;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import com.cinemaebooking.backend.showtime.application.dto.showtime.CreateShowtimeRequest;
import com.cinemaebooking.backend.showtime.application.dto.showtime.UpdateShowtimeRequest;
import com.cinemaebooking.backend.showtime.application.port.ShowtimeRepository;
import com.cinemaebooking.backend.showtime.domain.model.Showtime;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ShowtimeCommandValidator {

    private final ShowtimeRepository showtimeRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;

    private static final int MAX_EXTRA_MINUTES = 60;
    private static final int PREPARATION_MINUTES = 15;

    // ================== CREATE ==================

    public void validateCreateRequest(CreateShowtimeRequest request) {

        validateCreateInput(request);

        validateCreateFields(request);

        validateTimeWithDuration(request.getMovieId(), request.getStartTime(), request.getEndTime());

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
                .validate(request.getAudioLanguage(), "audioLanguage", profile.audioLanguageRules())
                .validate(request.getSubtitleLanguage(), "subtitleLanguage", profile.subtitleLanguageRules())
                .throwIfInvalid();
    }

    // ================== FIELD VALIDATION - UPDATE ==================

    private void validateUpdateFields(UpdateShowtimeRequest request) {

        var profile = ValidationFactory.showtime();

        ValidationEngine.of()
                .validate(request.getAudioLanguage(), "audioLanguage", profile.audioLanguageRules())
                .validate(request.getSubtitleLanguage(), "subtitleLanguage", profile.subtitleLanguageRules())
                .throwIfInvalid();
    }

    // ================== INPUTVALIDATION - TIME ==================

    private void validateTimeWithDuration(Long movieId, LocalDateTime startTime, LocalDateTime endTime) {
        Movie movie = movieRepository.findById(MovieId.of(movieId))
                .orElseThrow(() -> CommonExceptions.invalidInput("movieId", ErrorCategory.NOT_FOUND, "Phim không tồn tại"));


        // 1. Kiểm tra startTime < endTime
        if (!startTime.isBefore(endTime)) {
            throw CommonExceptions.invalidInput("endTime", ErrorCategory.INVALID_VALUE,
                    "Thời gian kết thúc phải sau thời gian bắt đầu");
        }

        int duration = movie.getDuration();
        LocalDateTime minEnd = startTime.plusMinutes(duration + PREPARATION_MINUTES);
        LocalDateTime maxEnd = minEnd.plusMinutes(MAX_EXTRA_MINUTES);

        // 2. endTime quá ngắn
        if (endTime.isBefore(minEnd)) {
            throw CommonExceptions.invalidInput("endTime", ErrorCategory.INVALID_VALUE,
                    String.format("Thời gian kết thúc phải từ %s trở đi (cần %d phút phim + %d phút chuẩn bị)",
                            formatTime(minEnd), duration, PREPARATION_MINUTES));
        }

        // 3. endTime quá dài
        if (endTime.isAfter(maxEnd)) {
            throw CommonExceptions.invalidInput("endTime", ErrorCategory.INVALID_VALUE,
                    String.format("Thời gian kết thúc tối đa là %s. Khoảng hợp lệ: %s – %s",
                            formatTime(maxEnd), formatTime(minEnd), formatTime(maxEnd)));
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

        Optional<Room> room = roomRepository.findById(RoomId.of(roomId));
        if (room.isEmpty()) throw RoomExceptions.notFound(RoomId.of(roomId));
        if (conflict) {
            throw CommonExceptions.invalidInput("startTime", ErrorCategory.INVALID_VALUE,
                    String.format("Phòng chiếu %s đã có suất chiếu khác trong khung giờ %s – %s vào ngày %s",
                            room.get().getName(),formatTime(startTime), formatTime(endTime), formatDate(startTime)));
        }
    }

    private String formatTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}