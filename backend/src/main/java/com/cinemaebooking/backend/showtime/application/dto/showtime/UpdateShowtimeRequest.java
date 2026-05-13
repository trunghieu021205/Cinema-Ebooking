package com.cinemaebooking.backend.showtime.application.dto.showtime;

import com.cinemaebooking.backend.showtime.domain.enums.Language;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShowtimeRequest {

    //private LocalDateTime startTime;
    //private LocalDateTime endTime;

    private Language audioLanguage;
    private Language subtitleLanguage;
}