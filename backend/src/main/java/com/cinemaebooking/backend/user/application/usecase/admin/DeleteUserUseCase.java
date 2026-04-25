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
public class DeleteUserUseCase {

    private final UserRepository userRepository;

    public void execute(UserId id) {

        if (id == null) {
            throw CommonExceptions.invalidInput("User id must not be null");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> UserExceptions.notFound(id));

        if (user.getStatus() == UserStatus.INACTIVE) {
            return;
        }

        user.changeStatus(UserStatus.INACTIVE);

        userRepository.update(user);
    }
}
