package com.cinemaebooking.backend.showtime.application.dto.showtime;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShowtimeRequest {

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String audioLanguage;
    private String subtitleLanguage;
}