package com.cinemaebooking.backend.user.application.usecase.auth;

import org.springframework.stereotype.Service;

@Service
public class LogoutUseCase {

    public void execute() {
        // Stateless JWT → logout handled at client side
        // Optionally implement blacklist later
    }
}
