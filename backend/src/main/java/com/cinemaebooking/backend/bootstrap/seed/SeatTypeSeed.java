package com.cinemaebooking.backend.bootstrap.seed;

import com.cinemaebooking.backend.room_layout.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.room_layout.domain.model.seatType.SeatType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatTypeSeed {

    private final SeatTypeRepository seatTypeRepository;

    public void seed() {
        createIfNotExists("STANDARD", BigDecimal.valueOf(80000));
        createIfNotExists("VIP",  BigDecimal.valueOf(120000));
        createIfNotExists("COUPLE",  BigDecimal.valueOf(160000));
    }

    private SeatType createIfNotExists(String name, BigDecimal price) {

        if (seatTypeRepository.existsByName(name)) {
            log.info("SeatType already exists: {}", name);
            return seatTypeRepository.findByNameIgnoreCase(name)
                    .orElseThrow(() -> new IllegalStateException("SeatType exists but not found"));
        }

        SeatType seatType = SeatType.builder()
                .name(name)
                .basePrice(price)
                .build();

        SeatType saved = seatTypeRepository.create(seatType);

        log.info("SeatType seeded: {}", name);
        return saved;
    }
}