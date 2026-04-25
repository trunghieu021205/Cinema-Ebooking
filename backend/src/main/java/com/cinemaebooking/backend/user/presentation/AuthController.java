package com.cinemaebooking.backend.user.presentation;

import com.cinemaebooking.backend.user.application.dto.AuthDTO.LoginRequest;
import com.cinemaebooking.backend.user.application.dto.AuthDTO.RefreshTokenRequest;
import com.cinemaebooking.backend.user.application.dto.AuthDTO.RegisterRequest;
import com.cinemaebooking.backend.user.application.dto.AuthDTO.ResetPasswordRequest;
import com.cinemaebooking.backend.user.application.dto.Response.LoginResponse;
import com.cinemaebooking.backend.user.application.usecase.auth.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final LogoutUseCase logoutUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request) {
        registerUseCase.execute(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return loginUseCase.execute(request);
    }

    @PostMapping("/logout")
    public void logout() {
        logoutUseCase.execute();
    }

    @PostMapping("/forgot_password")
    public void forgotPassword(@RequestParam String email) {
        forgotPasswordUseCase.execute(email);
    }

    @PostMapping("/reset_password")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordUseCase.execute(request);
    }

    @PostMapping("/refresh_token")
    public LoginResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return refreshTokenUseCase.execute(request);
    }
}