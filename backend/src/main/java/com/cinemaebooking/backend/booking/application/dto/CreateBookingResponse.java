package com.cinemaebooking.backend.booking.application.dto;

import com.cinemaebooking.backend.booking.domain.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingResponse {

    private Long bookingId;
    private String bookingCode;

    private BigDecimal totalTicketPrice;
    private BigDecimal totalComboPrice;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;

    private BookingStatus status;
    private LocalDateTime expiredAt;

    private List<Long> showTimeSeatIds;
}