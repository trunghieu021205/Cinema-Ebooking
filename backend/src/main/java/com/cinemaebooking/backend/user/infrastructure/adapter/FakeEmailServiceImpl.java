package com.cinemaebooking.backend.user.infrastructure.adapter;

import com.cinemaebooking.backend.user.application.port.EmailService;
import org.springframework.stereotype.Component;

@Component
public class FakeEmailServiceImpl implements EmailService {

    @Override
    public void send(String to, String subject, String body) {
        System.out.println("=== SEND EMAIL ===");
        System.out.println("To      : " + to);
        System.out.println("Subject : " + subject);
        System.out.println("Body    : " + body);
        System.out.println("==================");
    }
}
