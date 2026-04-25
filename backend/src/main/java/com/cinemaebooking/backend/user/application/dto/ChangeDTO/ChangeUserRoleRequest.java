package com.cinemaebooking.backend.user.application.dto.ChangeDTO;

import com.cinemaebooking.backend.user.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserRoleRequest {
    private UserRole role;
}
