package com.cinemaebooking.backend.user.application.usecase.auth;

import com.cinemaebooking.backend.common.exception.domain.UserExceptions;
import com.cinemaebooking.backend.user.application.port.EmailService;
import com.cinemaebooking.backend.user.application.port.JwtProvider;
import com.cinemaebooking.backend.user.application.port.UserRepository;
import com.cinemaebooking.backend.user.domain.model.User;
import com.cinemaebooking.backend.user.domain.valueObject.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForgotPasswordUseCase {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;

    public void execute(String email) {

        if (email == null || email.isBlank()) {
            throw UserExceptions.invalidEmail(email);
        }

        // 1. find user
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserExceptions::invalidCredentials);

        // 2. create reset-token (15 minutes)
        String resetToken = jwtProvider.generateResetToken(new UserId(user.getId().getValue()));

        // 3. generate reset link
        String link = "http://localhost:3000/reset-password?token=" + resetToken;

        // 4. send email
        emailService.send(
                email,
                "Reset Your Password",
                "Click the link to reset password: " + link
        );
    }
}