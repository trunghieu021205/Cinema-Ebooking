package com.cinemaebooking.backend.booking.domain.model;

import com.cinemaebooking.backend.booking.domain.enums.BookingStatus;
import com.cinemaebooking.backend.booking.domain.valueObject.BookingId;
import com.cinemaebooking.backend.booking_combo.domain.model.BookingCombo;
import com.cinemaebooking.backend.booking_coupon.domain.model.BookingCoupon;
import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.ticket.domain.model.Ticket;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
public class Booking extends BaseEntity<BookingId> {
    private final String bookingCode;
    private final Long userId;
    private final Long showtimeId;

    private final String movieTitle;
    private final String cinemaName;
    private final String roomName;
    private final LocalDateTime showtimeStartTime;
    private final LocalDateTime createdAt;
    @Builder.Default
    private List<Ticket> tickets = new ArrayList<>();
    @Builder.Default
    private List<BookingCombo> combos = new ArrayList<>();
    private BookingCoupon coupon;

    private BigDecimal totalTicketPrice;
    private BigDecimal totalComboPrice;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;

    private BookingStatus status;
    private final LocalDateTime expiredAt;
    private LocalDateTime paidAt;

    public void applyCoupon(BookingCoupon couponData) {
        if (couponData == null) {
            this.coupon = null;
            this.discountAmount = BigDecimal.ZERO;
            return;
        }

        this.coupon = couponData;

        // 2. Cập nhật số tiền giảm giá từ coupon
        // Lưu ý: Logic này có thể phức tạp hơn nếu giảm theo %
        this.discountAmount = couponData.getDiscountValue();

        // 3. (Optional) Kiểm tra xem số tiền giảm có lớn hơn tổng đơn không
        // Nếu giảm > tổng, thường ta sẽ chỉ giảm tối đa bằng tổng tiền ticket + combo
    }
    /**
     * Logic tính toán lại tổng tiền của toàn bộ Booking
     */
    public BigDecimal calculateSubtotal() {
        BigDecimal ticketSum = (tickets == null) ? BigDecimal.ZERO : tickets.stream()
                .map(Ticket::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal comboSum = (combos == null) ? BigDecimal.ZERO : combos.stream()
                .map(c -> c.getUnitPrice().multiply(BigDecimal.valueOf(c.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        this.totalTicketPrice = ticketSum;
        this.totalComboPrice = comboSum;

        return ticketSum.add(comboSum);
    }

    public void calculateTotal() {
        BigDecimal subTotal = calculateSubtotal(); // subtotal đã tính cả combo + ticket
        this.discountAmount = (coupon != null) ? coupon.getDiscountValue() : BigDecimal.ZERO;

        this.finalAmount = subTotal.subtract(this.discountAmount);

        if (this.finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            this.finalAmount = BigDecimal.ZERO;
        }
    }

    /**
     * Chuyển trạng thái sang đã xác nhận (Paid)
     */
    public void markAsPaid() {
        if (this.status != BookingStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể thanh toán đơn hàng đang chờ.");
        }
        this.status = BookingStatus.CONFIRMED;
        this.paidAt = LocalDateTime.now();
    }

    /**
     * Hủy đơn hàng
     */
    public void cancel() {
        if (this.status == BookingStatus.CANCELLED) return;

        if (this.status == BookingStatus.CONFIRMED) {
            throw new RuntimeException("Không thể hủy đơn hàng đã thanh toán.");
        }
        this.status = BookingStatus.CANCELLED;
    }

    public boolean isExpired() {
        return status == BookingStatus.PENDING &&
                expiredAt != null &&
                LocalDateTime.now().isAfter(expiredAt);
    }

    public void confirm() {
        if (this.status != BookingStatus.PENDING) {
            throw CommonExceptions.invalidInput(
                    "Chỉ booking PENDING mới có thể confirm."
            );
        }

        this.status = BookingStatus.CONFIRMED;
    }
}