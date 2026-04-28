package com.cinemaebooking.backend.coupon.application.validator;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.CouponExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import com.cinemaebooking.backend.coupon.application.dto.CreateCouponRequest;
import com.cinemaebooking.backend.coupon.application.dto.UpdateCouponRequest;
import com.cinemaebooking.backend.coupon.application.port.CouponRepository;
import com.cinemaebooking.backend.coupon.domain.valueobject.CouponId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponCommandValidator {

    private final CouponRepository couponRepository;

    // ================== CREATE ==================
    public void validateCreateRequest(CreateCouponRequest request) {
        if (request == null) {
            throw CommonExceptions.invalidInput("Request must not be null");
        }

        validateFields(request);
        validateDuplicateForCreate(request.getCode());
    }

    // ================== UPDATE ==================
    public void validateUpdateRequest(CouponId id, UpdateCouponRequest request) {
        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("Coupon id and request must not be null");
        }

        validateFields(request);
        validateDuplicateForUpdate(id, request.getCode());
    }

    // ================== FIELD VALIDATION ==================
    private void validateFields(Object request) {
        // Sử dụng ValidationProfile để kiểm tra format
        var profile = ValidationFactory.coupon();

        if (request instanceof CreateCouponRequest req) {
            ValidationEngine.of()
                    .validate(req.getCode(), "Coupon code", profile.codeRules())
                    .throwIfInvalid();
        } else if (request instanceof UpdateCouponRequest req) {
            ValidationEngine.of()
                    .validate(req.getCode(), "Coupon code", profile.codeRules())
                    .throwIfInvalid();
        }
        // Các field khác như value, limit sẽ được validate ở domain model
    }

    // ================== DUPLICATE CHECKS ==================
    private void validateDuplicateForCreate(String code) {
        if (couponRepository.existsByCode(code)) {
            throw CouponExceptions.alreadyExists(code);
        }
    }

    private void validateDuplicateForUpdate(CouponId id, String code) {
        if (couponRepository.existsByCodeAndIdNot(code, id)) {
            throw CouponExceptions.alreadyExists(code);
        }
    }
}