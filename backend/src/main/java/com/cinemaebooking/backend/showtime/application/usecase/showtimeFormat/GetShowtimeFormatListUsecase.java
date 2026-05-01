package com.cinemaebooking.backend.showtime.application.usecase.showtimeFormat;

import com.cinemaebooking.backend.showtime.application.dto.showtimeFormat.ShowtimeFormatResponse;
import com.cinemaebooking.backend.showtime.application.mapper.ShowtimeFormatResponseMapper;
import com.cinemaebooking.backend.showtime.application.port.ShowtimeFormatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetShowtimeFormatListUsecase {

    private final ShowtimeFormatRepository repository;
    private final ShowtimeFormatResponseMapper mapper;

    public Page<ShowtimeFormatResponse> execute(Pageable pageable) {

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }
}
