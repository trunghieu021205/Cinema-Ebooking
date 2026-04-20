package com.cinemaebooking.backend.bootstrap.seed;

import com.cinemaebooking.backend.bootstrap.reset.ResetService;
import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeedServiceImpl implements SeedService {
   // private final ResetService resetService;
    private final CinemaSeed cinemaSeed;
    private final RoomSeed roomSeed;
    @Override
    public void seed() {
        //resetService.reset();
        Cinema cinema = cinemaSeed.seed();
        roomSeed.seed(cinema.getId().getValue());
    }
}
