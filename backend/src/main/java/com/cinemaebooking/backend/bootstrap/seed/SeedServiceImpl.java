package com.cinemaebooking.backend.bootstrap.seed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeedServiceImpl implements SeedService {

    private final CinemaSeed cinemaSeed;

    @Override
    public void seed() {
        cinemaSeed.seed();
    }
}
