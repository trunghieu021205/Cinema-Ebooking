
package com.cinemaebooking.backend.booking_coupon.infrastructure.adapter;

import com.cinemaebooking.backend.booking_coupon.application.port.CouponInternalService;
import com.cinemaebooking.backend.booking_coupon.domain.model.BookingCoupon;
import com.cinemaebooking.backend.common.exception.domain.CouponExceptions;
import com.cinemaebooking.backend.coupon.application.port.CouponRepository;
import com.cinemaebooking.backend.coupon.domain.enums.CouponType;
import com.cinemaebooking.backend.coupon.domain.model.Coupon;
import com.cinemaebooking.backend.coupon.domain.valueobject.CouponId;
import com.cinemaebooking.backend.user_coupon.application.port.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponInternalServiceImpl implements CouponInternalService {

    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    @Override
    public BookingCoupon validateAndGetCoupon(Long userId, String couponCode, BigDecimal totalAmount) {
        // 1. Tìm quyền sở hữu của User
        var userCoupon = userCouponRepository.findByUserIdAndCode(userId, couponCode)
                .orElseThrow(() -> CouponExceptions.notFound(couponCode));

        // 2. Kiểm tra trạng thái UserCoupon (Status, usageRemain, expiredAt)
        if (!userCoupon.isUsable()) {
            throw CouponExceptions.invalidForBooking();
        }

        // 3. Lấy thông tin Coupon gốc để kiểm tra điều kiện áp dụng
        var coupon = couponRepository.findById(CouponId.of(userCoupon.getCouponId()))
                .orElseThrow(() -> CouponExceptions.notFound(couponCode));

        // 4. Kiểm tra giá trị đơn hàng tối thiểu
        if (totalAmount.compareTo(coupon.getMinimumBookingValue()) < 0) {
            throw CouponExceptions.invalidForBooking();
        }

        // 5. Tính toán số tiền được giảm thực tế (Snapshot discountValue)
        BigDecimal discount = calculateDiscount(coupon, totalAmount);

        return BookingCoupon.builder()
                .userCouponId(userCoupon.getId().getValue())
                .code(couponCode)
                .discountValue(discount)
                .appliedAt(LocalDateTime.now())
                .build();
    }

    private BigDecimal calculateDiscount(Coupon coupon, BigDecimal totalAmount) {
        BigDecimal discountValue = BigDecimal.ZERO;

        if (coupon.getType() == CouponType.FIXED) {
            // Trường hợp giảm số tiền cố định (ví dụ: 50k)
            discountValue = coupon.getValue();
        } else if (coupon.getType() == CouponType.PERCENT) {
            // Trường hợp giảm theo % (ví dụ: 10%)
            discountValue = totalAmount.multiply(coupon.getValue())
                    .divide(BigDecimal.valueOf(100));

            // Nếu có quy định số tiền giảm tối đa (Maximum Discount)
            if (coupon.getMaximumDiscountAmount() != null &&
                    discountValue.compareTo(coupon.getMaximumDiscountAmount()) > 0) {
                discountValue = coupon.getMaximumDiscountAmount();
            }
        }

        // Đảm bảo số tiền giảm không lớn hơn tổng tiền đơn hàng
        return discountValue.compareTo(totalAmount) > 0 ? totalAmount : discountValue;
    }
}