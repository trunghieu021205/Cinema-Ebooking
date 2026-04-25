package com.cinemaebooking.backend.user.application.usecase.admin;

import com.cinemaebooking.backend.common.exception.domain.UserExceptions;
import com.cinemaebooking.backend.user.application.port.UserRepository;
import com.cinemaebooking.backend.user.domain.model.User;
import com.cinemaebooking.backend.user.domain.valueObject.UserId;
import com.cinemaebooking.backend.user.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeUserRoleUseCase {

    private final UserRepository userRepository;

    public void execute(UserId userId, UserRole newRole) {

        if (userId == null || newRole == null) {
            throw UserExceptions.invalidRole();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserExceptions.notFound(userId));


        if (user.getRole() == newRole) {
            return;
        }

        user.changeRole(newRole);

        userRepository.update(user);
    }
}