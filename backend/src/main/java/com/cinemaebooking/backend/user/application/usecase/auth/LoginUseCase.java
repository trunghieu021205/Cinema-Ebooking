package com.cinemaebooking.backend.user.application.usecase.auth;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.UserExceptions;
import com.cinemaebooking.backend.user.application.dto.AuthDTO.LoginRequest;
import com.cinemaebooking.backend.user.application.dto.Response.LoginResponse;
import com.cinemaebooking.backend.user.application.port.JwtProvider;
import com.cinemaebooking.backend.user.application.port.PasswordEncoder;
import com.cinemaebooking.backend.user.application.port.UserRepository;
import com.cinemaebooking.backend.user.domain.enums.UserStatus;
import com.cinemaebooking.backend.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginResponse execute(LoginRequest request) {

        if (request == null) {
            throw CommonExceptions.invalidInput("Login request must not be null");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(UserExceptions::invalidCredentials);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw UserExceptions.invalidCredentials();
        }

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw UserExceptions.inactiveUser(user.getId());
        }

        String token = jwtProvider.generateToken(user.getId(), user.getRole().name());

        return new LoginResponse(token, user.getRole());
    }
}