package com.cinemaebooking.backend.user_coupon.application.validator;

import com.cinemaebooking.backend.common.exception.ErrorDetail;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.UserCouponExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationContext;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import com.cinemaebooking.backend.user_coupon.application.dto.RestoreUserCouponRequest;
import com.cinemaebooking.backend.user_coupon.application.port.UserCouponRepository;
import com.cinemaebooking.backend.user_coupon.domain.valueobject.UserCouponId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RestoreUserCouponValidator {

    private final UserCouponRepository userCouponRepository;

    public void validate(RestoreUserCouponRequest request) {
        if (request == null) {
            throw CommonExceptions.invalidInput("Restore request must not be null");
        }
        var profile = ValidationFactory.userCoupon();
        executeRules(request.getUserCouponId(), "userCouponId", profile.userCouponIdRules());

        UserCouponId id = UserCouponId.of(request.getUserCouponId());
        userCouponRepository.findById(id)
                .orElseThrow(() -> UserCouponExceptions.notFound(id));
    }

    private <T> void executeRules(T value, String fieldName, List<ValidationRule<T>> rules) {
        ValidationContext<T> context = new ValidationContext<>(value, fieldName);
        for (ValidationRule<T> rule : rules) {
            Optional<ErrorDetail> errorOpt = rule.validate(context);
            if (errorOpt.isPresent()) {
                ErrorDetail error = errorOpt.get();
                throw CommonExceptions.invalidInput(
                        error.getField() + ": " + error.getReason()
                );
            }
        }
    }
}