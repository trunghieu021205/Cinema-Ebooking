package com.cinemaebooking.backend.showtime.application.port;

import com.cinemaebooking.backend.showtime.domain.model.ShowtimeFormat;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeFormatId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ShowtimeFormatRepository {

    ShowtimeFormat create(ShowtimeFormat format);

    ShowtimeFormat update(ShowtimeFormat format);

    void deleteById(ShowtimeFormatId id);

    Optional<ShowtimeFormat> findById(ShowtimeFormatId id);

    Page<ShowtimeFormat> findAll(Pageable pageable);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, ShowtimeFormatId id);

    boolean existsById(ShowtimeFormatId id);
}
