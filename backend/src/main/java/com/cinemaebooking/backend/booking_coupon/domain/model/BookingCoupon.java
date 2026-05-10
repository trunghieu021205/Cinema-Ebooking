package com.cinemaebooking.backend.booking_coupon.domain.model;

import com.cinemaebooking.backend.booking_coupon.domain.valueObject.BookingCouponId;
import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * BookingCoupon Domain Model
 * Đại diện cho coupon đã được áp dụng thành công vào một Booking.
 */
@Getter
@SuperBuilder
public class BookingCoupon extends BaseEntity<BookingCouponId> {

    private final Long bookingId;    // Dùng Long để đồng bộ với Ticket và Combo
    private final Long userCouponId; // Tham chiếu tới Coupon cụ thể của User

    private final String code;       // Mã coupon tại thời điểm áp dụng
    private final BigDecimal discountValue; // Số tiền được giảm thực tế

    private final LocalDateTime appliedAt;

    // ================== BUSINESS METHODS ==================

    /**
     * Kiểm tra xem giá trị giảm giá có hợp lệ không
     */
    public void validate() {
        if (discountValue == null || discountValue.compareTo(BigDecimal.ZERO) < 0) {
            throw CommonExceptions.invalidInput("Giá trị giảm giá không được âm.");
        }
        if (code == null || code.isBlank()) {
            throw CommonExceptions.invalidInput("Mã coupon không được để trống.");
        }
    }

    /**
     * Tiện ích lấy ID (tương thích với Mapper DTO của bạn)
     */
    public Long getCouponId() {
        return this.userCouponId;
    }
}
