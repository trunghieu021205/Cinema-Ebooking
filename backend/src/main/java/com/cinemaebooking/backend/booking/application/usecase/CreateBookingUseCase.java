package com.cinemaebooking.backend.booking.application.usecase;

import com.cinemaebooking.backend.booking.application.dto.BookingDetailResponse;
import com.cinemaebooking.backend.booking.application.dto.CreateBookingRequest;
import com.cinemaebooking.backend.booking.application.mapper.BookingDetailResponseMapper;
import com.cinemaebooking.backend.booking.application.port.BookingRepository;
import com.cinemaebooking.backend.booking.domain.enums.BookingStatus;
import com.cinemaebooking.backend.booking.domain.model.Booking;

import com.cinemaebooking.backend.booking_combo.application.port.ComboInternalService;
import com.cinemaebooking.backend.booking_combo.domain.model.BookingCombo;
import com.cinemaebooking.backend.booking_coupon.application.port.CouponInternalService;
import com.cinemaebooking.backend.booking_coupon.domain.model.BookingCoupon;
import com.cinemaebooking.backend.showtime.application.port.ShowtimeInternalService;
import com.cinemaebooking.backend.ticket.domain.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateBookingUseCase {

    private final BookingRepository bookingRepository;
    private final BookingDetailResponseMapper mapper;

    private final ShowtimeInternalService showtimeService;
    private final ComboInternalService comboService;
    private final CouponInternalService couponService;

    // TODO: private final SeatLockInternalService seatLockService;

    @Transactional
    public BookingDetailResponse execute(CreateBookingRequest request) {

        // --- TODO [SEATLOCK INTEGRATION] ---
        // seatLockService.lockSeats(request.getShowtimeId(), request.getShowTimeSeatIds());
        // Bước này sẽ chặn đứng việc tranh chấp ghế ngay từ đầu luồng.
        // ------------------------------------

        // 1. Thu thập dữ liệu Snapshot từ các module khác
        var showtimeSnapshot = showtimeService.getSnapshot(request.getShowtimeId());

        List<Ticket> tickets = showtimeService.getTicketsBySeatIds(
                request.getShowtimeId(),
                request.getShowTimeSeatIds()
        );

        List<BookingCombo> combos = comboService.getBookingCombos(request.getCombos());

        // 2. Khởi tạo Domain Model bằng Builder
        Booking booking = Booking.builder()
                .bookingCode(generateBookingCode())
                .userId(request.getUserId())
                .showtimeId(request.getShowtimeId())
                .movieTitle(showtimeSnapshot.getMovieTitle())
                .cinemaName(showtimeSnapshot.getCinemaName())
                .roomName(showtimeSnapshot.getRoomName())
                .showtimeStartTime(showtimeSnapshot.getStartTime())
                .status(BookingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .tickets(tickets)
                .combos(combos)
                .build();

        // 3. Xử lý Coupon (Sử dụng trực tiếp logic tính toán từ Entity)
        if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
            // Lệnh tính toán tạm thời để lấy Subtotal làm đầu vào cho Coupon
            BigDecimal currentSubtotal = booking.calculateSubtotal();

            BookingCoupon couponData = couponService.validateAndGetCoupon(
                    request.getUserId(),
                    request.getCouponCode(),
                    currentSubtotal
            );
            booking.applyCoupon(couponData);
        }

        // 4. Tính toán tổng tiền cuối cùng và Lưu
        booking.calculateTotal();
        Booking savedBooking = bookingRepository.save(booking);

        return mapper.toDetailResponse(savedBooking);
    }

    private String generateBookingCode() {
        return "BOK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}