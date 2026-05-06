package com.cinemaebooking.backend.bootstrap.seed;

import com.cinemaebooking.backend.showtime.application.port.ShowtimeFormatRepository;
import com.cinemaebooking.backend.showtime.domain.model.ShowtimeFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShowtimeFormatSeed {

    private final ShowtimeFormatRepository showtimeFormatRepository;

    public void seed() {
        createIfNotExists("2D", BigDecimal.valueOf(0));
        createIfNotExists("3D", BigDecimal.valueOf(30000));
        createIfNotExists("IMAX", BigDecimal.valueOf(60000));
    }

    private ShowtimeFormat createIfNotExists(String name, BigDecimal extraPrice) {
        if (showtimeFormatRepository.existsByName(name)) {
            log.info("ShowtimeFormat already exists: {}", name);
            return showtimeFormatRepository.findByNameIgnoreCase(name)
                    .orElseThrow(() -> new IllegalStateException("ShowtimeFormat exists but not found"));
        }

        ShowtimeFormat format = ShowtimeFormat.builder()
                .name(name)
                .extraPrice(extraPrice)
                .build();

        ShowtimeFormat saved = showtimeFormatRepository.create(format);
        log.info("ShowtimeFormat seeded: {}", name);
        return saved;
    }
}