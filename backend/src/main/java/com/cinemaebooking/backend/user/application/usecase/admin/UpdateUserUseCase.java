package com.cinemaebooking.backend.user.application.usecase.admin;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.UserExceptions;
import com.cinemaebooking.backend.user.application.dto.Response.UserResponse;
import com.cinemaebooking.backend.user.application.dto.UserDTO.AdminUpdateUserRequest;
import com.cinemaebooking.backend.user.application.mapper.UserResponseMapper;
import com.cinemaebooking.backend.user.application.port.UserRepository;
import com.cinemaebooking.backend.user.application.validator.User.UserCommandValidator;
import com.cinemaebooking.backend.user.domain.model.User;
import com.cinemaebooking.backend.user.domain.valueObject.UserId;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserRepository userRepository;
    private final UserResponseMapper mapper;
    private final UserCommandValidator validator;

    public UserResponse execute(UserId id, AdminUpdateUserRequest request) {

        // ================== VALIDATION ==================
        validator.validateAdminUpdateRequest(id, request);

        // ================== LOAD ==================
        User user = loadUser(id);

        // ================== APPLY DOMAIN ==================
        applyUpdate(user, request);

        // ================== PERSIST ==================
        User saved = persist(user);

        // ================== RESPONSE ==================
        return mapper.toUserResponse(saved);
    }

    // ================== PRIVATE METHODS ==================

    private User loadUser(UserId id) {
        return userRepository.findById(id)
                .orElseThrow(() -> UserExceptions.notFound(id));
    }

    private void applyUpdate(User user, AdminUpdateUserRequest request) {

        user.updateProfile(
                request.getFullName(),
                request.getPhoneNumber(),
                request.getAvatarUrl()
        );

        if (request.getRole() != null) {
            user.changeRole(request.getRole());
        }

        if (request.getStatus() != null) {
            user.changeStatus(request.getStatus());
        }
    }

    private User persist(User user) {
        try {
            return userRepository.update(user);
        } catch (OptimisticLockException | ObjectOptimisticLockingFailureException ex) {
            throw CommonExceptions.concurrencyConflict();
        }
    }
}