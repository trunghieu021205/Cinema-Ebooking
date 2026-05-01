package com.cinemaebooking.backend.showtime.application.dto.showtime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateShowtimeRequest {

    private Long movieId;
    private Long roomId;
    private Long formatId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String audioLanguage;
    private String subtitleLanguage;
}