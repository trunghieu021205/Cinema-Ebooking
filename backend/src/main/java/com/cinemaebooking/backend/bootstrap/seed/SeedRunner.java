package com.cinemaebooking.backend.bootstrap.seed;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
@Profile("local")
@Component
@RequiredArgsConstructor
public class SeedRunner implements CommandLineRunner {

    private final SeedService seedService;

    @Override
    public void run(String... args) {
        //seedService.seed();
    }
}