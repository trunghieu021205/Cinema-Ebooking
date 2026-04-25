package com.cinemaebooking.backend.user.application.usecase.auth;

import com.cinemaebooking.backend.common.exception.domain.UserExceptions;
import com.cinemaebooking.backend.user.application.dto.AuthDTO.ResetPasswordRequest;
import com.cinemaebooking.backend.user.application.port.JwtProvider;
import com.cinemaebooking.backend.user.application.port.PasswordEncoder;
import com.cinemaebooking.backend.user.application.port.UserRepository;
import com.cinemaebooking.backend.user.domain.model.User;
import com.cinemaebooking.backend.user.domain.valueObject.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordUseCase {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void execute(ResetPasswordRequest request) {

        if (request == null) {
            throw UserExceptions.invalidCredentials();
        }

        String token = request.getToken();
        String newPassword = request.getNewPassword();

        if (token == null || token.isBlank()) {
            throw UserExceptions.invalidCredentials();
        }

        if (newPassword == null || newPassword.isBlank()) {
            throw UserExceptions.invalidPassword();
        }

        // 1. extract userId từ token
        UserId userId = jwtProvider.extractUserId(token);

        // 2. find user
        User user = userRepository.findById(userId)
                .orElseThrow(UserExceptions::unauthorized);

        // 3. encode password
        String hashedPassword = passwordEncoder.encode(newPassword);

        // 4. update domain
        user.changePassword(hashedPassword);

        // 5. save
        userRepository.update(user);
    }
}