package com.cinemaebooking.backend.showtime.application.dto.showtime;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ShowtimeResponse {

    private Long id;

    private Long movieId;
    private Long roomId;
    private Long formatId;
    private Long cinemaId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String audioLanguage;
    private String subtitleLanguage;

    private String status;
}