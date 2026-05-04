package com.cinemaebooking.backend.showtime.application.mapper;

import com.cinemaebooking.backend.showtime.application.dto.showtimeFormat.ShowtimeFormatResponse;
import com.cinemaebooking.backend.showtime.domain.model.ShowtimeFormat;
import org.springframework.stereotype.Component;

@Component
public class ShowtimeFormatResponseMapper {

    public ShowtimeFormatResponse toResponse(ShowtimeFormat format) {
        return ShowtimeFormatResponse.builder()
                .id(format.getId().getValue())
                .name(format.getName())
                .extraPrice(format.getExtraPrice())
                .build();
    }
}
