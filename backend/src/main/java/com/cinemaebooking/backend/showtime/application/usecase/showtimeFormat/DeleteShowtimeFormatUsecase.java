package com.cinemaebooking.backend.showtime.application.usecase.showtimeFormat;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.ShowtimeFormatExceptions;
import com.cinemaebooking.backend.showtime.application.port.ShowtimeFormatRepository;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeFormatId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteShowtimeFormatUsecase {

    private final ShowtimeFormatRepository repository;

    public void execute(ShowtimeFormatId id) {

        if (id == null) {
            throw CommonExceptions.invalidInput("ShowtimeFormatId must not be null");
        }

        if (!repository.existsById(id)) {
            throw ShowtimeFormatExceptions.notFound(id);
        }

        repository.deleteById(id);
    }
}
