package com.cinemaebooking.backend.user.application.usecase.auth;

import com.cinemaebooking.backend.common.exception.domain.UserExceptions;
import com.cinemaebooking.backend.user.application.dto.AuthDTO.RefreshTokenRequest;
import com.cinemaebooking.backend.user.application.dto.Response.LoginResponse;
import com.cinemaebooking.backend.user.application.port.JwtProvider;
import com.cinemaebooking.backend.user.application.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public LoginResponse execute(RefreshTokenRequest request) {
        if (request == null || request.getRefreshToken() == null) {
            throw UserExceptions.invalidCredentials();
        }

        String oldRefreshToken = request.getRefreshToken();

        // 1. Validate refresh token (signature + expiration) and extract userId
        var userId = jwtProvider.extractUserId(oldRefreshToken);

        // 2. Load user to get role
        var user = userRepository.findById(userId)
                .orElseThrow(UserExceptions::unauthorized);

        // 3. Generate new access token (short-lived)
        String newAccessToken = jwtProvider.generateToken(user.getId(), user.getRole().name());

        // 4. Generate new refresh token (rotation)
        String newRefreshToken = jwtProvider.generateRefreshToken(user.getId().getValue());

        // 5. Return both tokens + role
        return new LoginResponse(newAccessToken, newRefreshToken, user.getRole().name());
    }
}