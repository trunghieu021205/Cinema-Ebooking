package com.cinemaebooking.backend.booking_coupon.application.usecase;

import com.cinemaebooking.backend.booking_coupon.application.dto.BookingCouponResponse;
import com.cinemaebooking.backend.booking_coupon.application.mapper.BookingCouponResponseMapper;
import com.cinemaebooking.backend.booking_coupon.application.port.BookingCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetBookingCouponByBookingUseCase{

    private final BookingCouponRepository bookingCouponRepository;
    private final BookingCouponResponseMapper mapper;

    @Transactional(readOnly = true)
    public BookingCouponResponse execute(Long bookingId) {
        // Trả về DTO hoặc null nếu booking đó không áp dụng coupon
        return bookingCouponRepository.findByBookingId(bookingId)
                .map(mapper::toResponse)
                .orElse(null);
        // Ở đây dùng orElse(null) vì không phải đơn hàng nào cũng có coupon
    }
}
