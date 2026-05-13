package com.cinemaebooking.backend.ticket.presentation;

import com.cinemaebooking.backend.ticket.application.dto.CheckInTicketRequest;
import com.cinemaebooking.backend.ticket.application.dto.TicketResponse;
import com.cinemaebooking.backend.ticket.application.usecase.CheckInTicketUseCase;
import com.cinemaebooking.backend.ticket.application.usecase.GetTicketByCodeUseCase;
import com.cinemaebooking.backend.ticket.application.usecase.GetTicketsByBookingUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TicketController - REST API for Ticket resource.
 * Responsibility:
 * - Handle HTTP requests for tickets
 * - Delegate to specific use cases (Check-in, View details)
 * - Return domain-specific responses
 */
@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final CheckInTicketUseCase checkInTicketUseCase;
    private final GetTicketByCodeUseCase getTicketByCodeUseCase;
    private final GetTicketsByBookingUseCase getTicketsByBookingUseCase;

    // ================== CHECK-IN ==================
    /**
     * Thực hiện soát vé cho khách vào phòng chiếu.
     * Thường yêu cầu quyền STAFF hoặc ADMIN.
     */
    @PostMapping("/check-in")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public TicketResponse checkIn(@Valid @RequestBody CheckInTicketRequest request) {
        return checkInTicketUseCase.execute(request);
    }

    // ================== DETAIL BY CODE ==================
    /**
     * Lấy thông tin chi tiết vé thông qua mã Ticket Code (dùng để hiển thị QR).
     */
    @GetMapping("/{ticketCode}")
    public TicketResponse getTicketByCode(@PathVariable String ticketCode) {
        return getTicketByCodeUseCase.execute(ticketCode);
    }

    // ================== LIST BY BOOKING ==================
    /**
     * Lấy danh sách tất cả các vé thuộc về một Booking cụ thể.
     */
    @GetMapping("/booking/{bookingId}")
    public List<TicketResponse> getTicketsByBooking(@PathVariable Long bookingId) {
        return getTicketsByBookingUseCase.execute(bookingId);
    }
}