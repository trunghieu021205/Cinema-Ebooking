package com.cinemaebooking.backend.user.application.usecase.admin;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.UserExceptions;
import com.cinemaebooking.backend.user.application.port.UserRepository;
import com.cinemaebooking.backend.user.domain.enums.UserStatus;
import com.cinemaebooking.backend.user.domain.model.User;
import com.cinemaebooking.backend.user.domain.valueObject.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeactivateUserUseCase {

    private final UserRepository userRepository;

    public void execute(UserId userId) {

        if (userId == null) {
            throw CommonExceptions.invalidInput("UserId must not be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserExceptions.notFound(userId));

        if (user.getStatus() == UserStatus.INACTIVE) {
            return;
        }

        user.changeStatus(UserStatus.INACTIVE);

        userRepository.update(user);
    }
}
