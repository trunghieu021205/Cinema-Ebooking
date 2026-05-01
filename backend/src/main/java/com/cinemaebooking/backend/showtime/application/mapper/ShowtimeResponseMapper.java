package com.cinemaebooking.backend.showtime.application.mapper;

import com.cinemaebooking.backend.showtime.application.dto.showtime.ShowtimeResponse;
import com.cinemaebooking.backend.showtime.domain.model.Showtime;
import org.springframework.stereotype.Component;

@Component
public class ShowtimeResponseMapper {

    public ShowtimeResponse toResponse(Showtime showtime, Long cinemaId) {
        if (showtime == null) return null;

        return ShowtimeResponse.builder()
                .id(showtime.getId() != null ? showtime.getId().getValue() : null)
                .movieId(showtime.getMovieId())
                .roomId(showtime.getRoomId())
                .cinemaId(cinemaId)
                .formatId(showtime.getFormatId())
                .startTime(showtime.getStartTime())
                .endTime(showtime.getEndTime())
                .audioLanguage(showtime.getAudioLanguage())
                .subtitleLanguage(showtime.getSubtitleLanguage())
                .status(showtime.getStatus().name())
                .build();
    }
}