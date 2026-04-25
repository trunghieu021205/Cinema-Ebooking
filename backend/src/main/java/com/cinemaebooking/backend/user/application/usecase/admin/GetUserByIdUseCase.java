package com.cinemaebooking.backend.user.application.usecase.admin;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.user.application.dto.Response.UserResponse;
import com.cinemaebooking.backend.user.application.mapper.UserResponseMapper;
import com.cinemaebooking.backend.user.application.port.UserRepository;
import com.cinemaebooking.backend.user.domain.valueObject.UserId;
import com.cinemaebooking.backend.common.exception.domain.UserExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByIdUseCase {

    private final UserRepository userRepository;
    private final UserResponseMapper mapper;

    public UserResponse execute(UserId id) {

        // ================== VALIDATION ==================
        if (id == null) {
            throw CommonExceptions.invalidInput("User id must not be null");
        }

        // ================== LOAD ==================
        return userRepository.findById(id)
                .map(mapper::toUserResponse)
                .orElseThrow(() -> UserExceptions.notFound(id));
    }
}