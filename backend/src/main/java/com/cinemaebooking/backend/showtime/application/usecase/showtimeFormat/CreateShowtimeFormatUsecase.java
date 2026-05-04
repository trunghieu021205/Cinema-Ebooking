package com.cinemaebooking.backend.showtime.application.usecase.showtimeFormat;

import com.cinemaebooking.backend.showtime.application.dto.showtimeFormat.CreateShowtimeFormatRequest;
import com.cinemaebooking.backend.showtime.application.dto.showtimeFormat.ShowtimeFormatResponse;
import com.cinemaebooking.backend.showtime.application.mapper.ShowtimeFormatResponseMapper;
import com.cinemaebooking.backend.showtime.application.port.ShowtimeFormatRepository;
import com.cinemaebooking.backend.showtime.application.validator.ShowtimeFormatCommandValidator;
import com.cinemaebooking.backend.showtime.domain.model.ShowtimeFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateShowtimeFormatUsecase {

    private final ShowtimeFormatRepository repository;
    private final ShowtimeFormatCommandValidator validator;
    private final ShowtimeFormatResponseMapper mapper;

    public ShowtimeFormatResponse execute(CreateShowtimeFormatRequest request) {
        validator.validateCreate(request);

        ShowtimeFormat format = ShowtimeFormat.builder()
                .name(request.getName().trim())
                .extraPrice(request.getExtraPrice())
                .build();

        ShowtimeFormat saved = repository.create(format);

        return mapper.toResponse(saved);
    }
}
