package com.cinemaebooking.backend.bootstrap.seed;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeedRunner implements CommandLineRunner {

    private final SeedService seedService;

    @Override
    public void run(String... args) {
        seedService.seed();
    }
}