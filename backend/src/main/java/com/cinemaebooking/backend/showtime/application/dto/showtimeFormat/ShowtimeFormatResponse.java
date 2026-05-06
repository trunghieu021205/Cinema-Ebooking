package com.cinemaebooking.backend.showtime.application.dto.showtimeFormat;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ShowtimeFormatResponse {
    private Long id;
    private String name;
    private BigDecimal extraPrice;
}
