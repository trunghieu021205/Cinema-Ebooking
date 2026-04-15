package com.cinemaebooking.backend.bootstrap.reset;

import com.cinemaebooking.backend.room.infrastructure.persistence.repository.RoomJpaRepository;
import com.cinemaebooking.backend.cinema.infrastructure.persistence.repository.CinemaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetService {
    private final RoomJpaRepository roomRepository;
    private final CinemaJpaRepository cinemaRepository;

    public void reset() {
        roomRepository.deleteAll();
        cinemaRepository.deleteAll();
    }
}