package com.cinemaebooking.backend.showtime.application.validator;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.ShowtimeFormatExceptions;
import com.cinemaebooking.backend.showtime.application.dto.showtimeFormat.CreateShowtimeFormatRequest;
import com.cinemaebooking.backend.showtime.application.dto.showtimeFormat.UpdateShowtimeFormatRequest;
import com.cinemaebooking.backend.showtime.application.port.ShowtimeFormatRepository;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeFormatId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShowtimeFormatCommandValidator {

    private final ShowtimeFormatRepository repository;

    // ================== CREATE ==================

    public void validateCreate(CreateShowtimeFormatRequest request) {

        if (request == null) {
            throw CommonExceptions.invalidInput("Request must not be null");
        }

        validateName(request.getName());
        validatePrice(request.getExtraPrice());

        if (repository.existsByName(request.getName())) {
            throw ShowtimeFormatExceptions.duplicateName(request.getName());
        }
    }

    // ================== UPDATE ==================

    public void validateUpdate(ShowtimeFormatId id, UpdateShowtimeFormatRequest request) {

        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("Id and request must not be null");
        }

        validateName(request.getName());
        validatePrice(request.getExtraPrice());

        if (repository.existsByNameAndIdNot(request.getName(), id)) {
            throw ShowtimeFormatExceptions.duplicateName(request.getName());
        }
    }

    // ================== PRIVATE ==================

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw ShowtimeFormatExceptions.invalidName(name);
        }
    }

    private void validatePrice(Long price) {
        if (price == null || price < 0) {
            throw ShowtimeFormatExceptions.invalidPrice(price);
        }
    }
}