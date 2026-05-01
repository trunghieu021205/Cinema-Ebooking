package com.cinemaebooking.backend.showtime.application.usecase.showtimeFormat;

import com.cinemaebooking.backend.common.exception.domain.ShowtimeFormatExceptions;
import com.cinemaebooking.backend.showtime.application.dto.showtimeFormat.ShowtimeFormatResponse;
import com.cinemaebooking.backend.showtime.application.dto.showtimeFormat.UpdateShowtimeFormatRequest;
import com.cinemaebooking.backend.showtime.application.mapper.ShowtimeFormatResponseMapper;
import com.cinemaebooking.backend.showtime.application.port.ShowtimeFormatRepository;
import com.cinemaebooking.backend.showtime.application.validator.ShowtimeFormatCommandValidator;
import com.cinemaebooking.backend.showtime.domain.model.ShowtimeFormat;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeFormatId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateShowtimeFormatUsecase {

    private final ShowtimeFormatRepository repository;
    private final ShowtimeFormatCommandValidator validator;
    private final ShowtimeFormatResponseMapper mapper;

    public ShowtimeFormatResponse execute(
            ShowtimeFormatId id,
            UpdateShowtimeFormatRequest request
    ) {
        validator.validateUpdate(id, request);
        ShowtimeFormat format = repository.findById(id)
                .orElseThrow(() -> ShowtimeFormatExceptions.notFound(id));

        format.update(
                request.getName().trim(),
                request.getExtraPrice()
        );

        ShowtimeFormat updated = repository.update(format);

        return mapper.toResponse(updated);
    }
}
