package com.cinemaebooking.backend.user_coupon.infrastructure.adapter;

import com.cinemaebooking.backend.coupon.application.dto.CouponResponse;
import com.cinemaebooking.backend.coupon.application.usecase.GetCouponDetailUseCase;
import com.cinemaebooking.backend.coupon.domain.valueobject.CouponId;
import com.cinemaebooking.backend.user_coupon.application.port.CouponPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CouponAdapter implements CouponPort {

    // Tiêm UseCase của module Coupon vào
    private final GetCouponDetailUseCase getCouponDetailUseCase;

    @Override
    public Optional<CouponSnapshot> findValidCoupon(Long couponId, LocalDateTime now) {
        try {
            // 1. Gọi sang module Coupon để lấy chi tiết mã giảm giá
            CouponResponse couponResponse = getCouponDetailUseCase.execute(CouponId.of(couponId));

            // 2. Chuyển đổi dữ liệu (Mapping)

            // Xử lý logic trạng thái hoạt động (active)
            // Dựa vào CouponResponse, ta xét Coupon là active nếu ngày hiện tại nằm trong khoảng [startDate, endDate]
            LocalDate today = now.toLocalDate();
            boolean isActive = true;
            if (couponResponse.getStartDate() != null && today.isBefore(couponResponse.getStartDate())) {
                isActive = false;
            }
            if (couponResponse.getEndDate() != null && today.isAfter(couponResponse.getEndDate())) {
                isActive = false;
            }

            // Chuyển đổi LocalDate (endDate) sang LocalDateTime để khớp với record CouponSnapshot
            LocalDateTime expiryDate = null;
            if (couponResponse.getEndDate() != null) {
                // Giả định coupon hết hạn vào cuối ngày (23:59:59) của ngày endDate
                expiryDate = couponResponse.getEndDate().atTime(23, 59, 59);
            }

            // Xử lý các trường có thể null bằng giá trị mặc định an toàn
            int pointCost = couponResponse.getPointsToRedeem() != null ? couponResponse.getPointsToRedeem() : 0;
            int maxUsagePerUser = couponResponse.getPerUserUsage() != null ? couponResponse.getPerUserUsage() : 1;

            // 3. Tạo và trả về CouponSnapshot
            CouponSnapshot snapshot = new CouponSnapshot(
                    couponResponse.getId(),
                    isActive,
                    expiryDate,
                    pointCost,
                    maxUsagePerUser
            );

            return Optional.of(snapshot);

        } catch (Exception e) {
            // Nếu GetCouponDetailUseCase ném lỗi (vd: CouponExceptions.notFound)
            // thì trả về rỗng để Validator bên UserCoupon tự quăng lỗi tương ứng
            return Optional.empty();
        }
    }
}