package com.cinemaebooking.backend.user.application.dto.ChangeDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    private String oldPassword;
    private String newPassword;
}
