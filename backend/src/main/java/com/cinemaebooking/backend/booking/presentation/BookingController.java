package com.cinemaebooking.backend.booking.presentation;

import com.cinemaebooking.backend.booking.application.dto.BookingDetailResponse;
import com.cinemaebooking.backend.booking.application.dto.CreateBookingRequest;
import com.cinemaebooking.backend.booking.application.usecase.CancelBookingUseCase;
import com.cinemaebooking.backend.booking.application.usecase.ConfirmPaymentUseCase;
import com.cinemaebooking.backend.booking.application.usecase.CreateBookingUseCase;
import com.cinemaebooking.backend.booking.application.usecase.GetBookingDetailUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * BookingController - REST API cho tài nguyên Đặt vé.
 * Trách nhiệm:
 * - Tiếp nhận HTTP requests
 * - Điều phối đến các Use Cases tương ứng
 * - Trả về Response DTO phù hợp
 *
 * @author YourName
 * @since 2026
 */
@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final CreateBookingUseCase createBookingUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;
    private final ConfirmPaymentUseCase confirmPaymentUseCase;
    private final GetBookingDetailUseCase getBookingDetailUseCase;

    // ================== CREATE (ĐẶT VÉ TẠM THỜI) ==================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDetailResponse createBooking(@Valid @RequestBody CreateBookingRequest request) {
        return createBookingUseCase.execute(request);
    }

    // ================== DETAIL (XEM CHI TIẾT) ==================
    @GetMapping("/{id}")
    public BookingDetailResponse getBookingDetail(@PathVariable Long id) {
        return getBookingDetailUseCase.execute(id);
    }

    // ================== CANCEL (HỦY ĐẶT VÉ) ==================
    @PostMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public void cancelBooking(@PathVariable Long id) {
        cancelBookingUseCase.execute(id);
    }

    // ================== CONFIRM PAYMENT (XÁC NHẬN THANH TOÁN) ==================
    /**
     * Endpoint này thường được gọi sau khi có kết quả từ Gateway thanh toán
     */
    @PostMapping("/{id}/confirm-payment")
    @ResponseStatus(HttpStatus.OK)
    public void confirmPayment(@PathVariable Long id) {
         confirmPaymentUseCase.execute(id);
    }
}
