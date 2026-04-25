package com.cinemaebooking.backend.user.application.port;

public interface EmailService {
    void send(String to, String subject, String body);
}

