package com.cinemaebooking.backend.user.application.usecase.admin;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.user.application.dto.Response.UserResponse;
import com.cinemaebooking.backend.user.application.mapper.UserResponseMapper;
import com.cinemaebooking.backend.user.application.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllUsersUseCase {

    private final UserRepository userRepository;
    private final UserResponseMapper mapper;

    public Page<UserResponse> execute(Pageable pageable) {

        if (pageable == null) {
            throw CommonExceptions.invalidInput("Pageable must not be null");
        }

        return userRepository.findAll(pageable)
                .map(mapper::toUserResponse);
    }
}