package com.cinemaebooking.backend.showtime.application.dto.showtimeFormat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateShowtimeFormatRequest {
    private String name;
    private Long extraPrice;
}
