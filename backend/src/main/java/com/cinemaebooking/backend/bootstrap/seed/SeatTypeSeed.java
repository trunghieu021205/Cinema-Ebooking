package com.cinemaebooking.backend.bootstrap.seed;

import com.cinemaebooking.backend.seat.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.seat.domain.model.seatType.SeatType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatTypeSeed {

    private final SeatTypeRepository seatTypeRepository;

    public void seed() {
        createIfNotExists("STANDARD", 80_000L);
        createIfNotExists("VIP", 120_000L);
        createIfNotExists("COUPLE", 160_000L);
    }

    private SeatType createIfNotExists(String name, Long price) {

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