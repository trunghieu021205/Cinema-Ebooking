package com.cinemaebooking.backend.showtime.application.dto.showtimeFormat;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateShowtimeFormatRequest {
    private String name;
    private Long extraPrice;
}
