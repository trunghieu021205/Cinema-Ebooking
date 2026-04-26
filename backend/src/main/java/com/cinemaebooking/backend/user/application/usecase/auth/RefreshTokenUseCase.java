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

        String refreshToken = request.getRefreshToken();

        // 1. extract userId
        var userId = jwtProvider.extractUserId(refreshToken);

        // 2. load user (ĐỂ LẤY ROLE)
        var user = userRepository.findById(userId)
                .orElseThrow(UserExceptions::unauthorized);

        // 3. generate new access token
        String token = jwtProvider.generateToken(user.getId(), user.getRole().name());

        // 4. return full response
        return new LoginResponse(
                token,
                user.getRole()
        );
    }
}
