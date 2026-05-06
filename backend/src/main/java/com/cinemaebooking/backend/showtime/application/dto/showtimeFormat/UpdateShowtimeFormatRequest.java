package com.cinemaebooking.backend.showtime.application.dto.showtimeFormat;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class UpdateShowtimeFormatRequest {
    private String name;
    private BigDecimal extraPrice;
}
