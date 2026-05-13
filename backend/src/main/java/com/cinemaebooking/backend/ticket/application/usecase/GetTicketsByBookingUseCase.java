package com.cinemaebooking.backend.ticket.application.usecase;


import com.cinemaebooking.backend.ticket.application.dto.TicketResponse;
import com.cinemaebooking.backend.ticket.application.mapper.TicketResponseMapper;
import com.cinemaebooking.backend.ticket.application.port.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * GetTicketsByBookingUseCaseImpl
 * Responsibility:
 * - Truy xuất danh sách vé thuộc về một đơn hàng (Booking)
 * - Chuyển đổi sang danh sách DTO để trả về cho Presentation layer
 */
@Service
@RequiredArgsConstructor
public class GetTicketsByBookingUseCase {

    private final TicketRepository ticketRepository;
    private final TicketResponseMapper ticketResponseMapper;

    @Transactional(readOnly = true)
    public List<TicketResponse> execute(Long bookingId) {
        // 1. Lấy danh sách từ repository port
        // 2. Sử dụng Stream API kết hợp với Mapper thủ công bạn đã viết để chuyển đổi
        return ticketRepository.findByBookingId(bookingId)
                .stream()
                .map(ticketResponseMapper::toTicketResponse)
                .collect(Collectors.toList());
    }
}
