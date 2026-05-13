package com.cinemaebooking.backend.showtime.application.dto.showtimeFormat;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateShowtimeFormatRequest {
    private String name;
    private BigDecimal extraPrice;
}
