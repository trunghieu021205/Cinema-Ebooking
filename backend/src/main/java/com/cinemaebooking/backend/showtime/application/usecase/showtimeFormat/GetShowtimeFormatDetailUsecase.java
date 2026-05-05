package com.cinemaebooking.backend.showtime.application.usecase.showtimeFormat;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.ShowtimeFormatExceptions;
import com.cinemaebooking.backend.showtime.application.dto.showtimeFormat.ShowtimeFormatResponse;
import com.cinemaebooking.backend.showtime.application.mapper.ShowtimeFormatResponseMapper;
import com.cinemaebooking.backend.showtime.application.port.ShowtimeFormatRepository;
import com.cinemaebooking.backend.showtime.domain.model.ShowtimeFormat;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeFormatId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetShowtimeFormatDetailUsecase {

    private final ShowtimeFormatRepository repository;
    private final ShowtimeFormatResponseMapper mapper;

    public ShowtimeFormatResponse execute(ShowtimeFormatId id) {

        if (id == null) {
            throw CommonExceptions.invalidInput("Id must not be null");
        }

        ShowtimeFormat format = repository.findById(id)
                .orElseThrow(() -> ShowtimeFormatExceptions.notFound(id));

        return mapper.toResponse(format);
    }
}
