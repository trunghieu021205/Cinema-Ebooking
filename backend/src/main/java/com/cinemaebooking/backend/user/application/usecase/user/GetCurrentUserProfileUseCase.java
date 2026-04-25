package com.cinemaebooking.backend.user.application.usecase.user;

import com.cinemaebooking.backend.common.exception.domain.UserExceptions;
import com.cinemaebooking.backend.user.application.dto.Response.UserResponse;
import com.cinemaebooking.backend.user.application.mapper.UserResponseMapper;
import com.cinemaebooking.backend.user.application.port.UserRepository;
import com.cinemaebooking.backend.user.domain.model.User;
import com.cinemaebooking.backend.user.domain.valueObject.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCurrentUserProfileUseCase {

    private final UserRepository userRepository;
    private final UserResponseMapper mapper;

    public UserResponse execute(Long userIdRaw) {

        UserId userId = UserId.of(userIdRaw);

        User user = userRepository.findById(userId)
                .orElseThrow(UserExceptions::unauthorized);

        return mapper.toUserResponse(user);
    }
}