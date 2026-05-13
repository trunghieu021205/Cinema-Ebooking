package com.cinemaebooking.backend.booking_combo.domain.model;

import com.cinemaebooking.backend.booking_combo.domain.valueObject.BookingComboId;
import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.booking.domain.valueObject.BookingId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * BookingCombo Domain Model
 * Tương tự Ticket, nó giữ thông tin bắp nước đi kèm với một Booking cụ thể.
 */
@Getter
@SuperBuilder
public class BookingCombo extends BaseEntity<BookingComboId> {

    private Long bookingId;
    private Long comboId;

    private String comboName;
    private BigDecimal unitPrice;

    private Integer quantity;
    private BigDecimal totalPrice;

    // ================== BUSINESS METHODS ==================

    /**
     * Cập nhật số lượng và tự động tính lại tổng tiền cho combo này
     */
    public void updateQuantity(Integer newQuantity) {
        if (newQuantity == null || newQuantity <= 0) {
            throw CommonExceptions.invalidInput("Số lượng combo phải lớn hơn 0");
        }
        this.quantity = newQuantity;
        this.calculateTotalPrice();
    }

    /**
     * Logic tính tiền nội bộ của Combo
     */
    public void calculateTotalPrice() {
        if (this.unitPrice != null && this.quantity != null) {
            this.totalPrice = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
        } else {
            this.totalPrice = BigDecimal.ZERO;
        }
    }
}
