package com.cinemaebooking.backend.user.application.dto.AuthDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;
}