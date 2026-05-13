package com.cinemaebooking.backend.ticket.application.mapper;

import com.cinemaebooking.backend.ticket.application.dto.TicketResponse;
import com.cinemaebooking.backend.ticket.domain.model.Ticket;
import org.springframework.stereotype.Component;

/**
 * TicketResponseMapper - Chuyển đổi dữ liệu từ Domain sang Response DTO.
 * Lưu ý: Tương thích với cấu trúc Domain Model Ticket sử dụng TicketId (Value Object).
 */
@Component
public class TicketResponseMapper {

    public TicketResponse toTicketResponse(Ticket ticket) {
        if (ticket == null) {
            return null;
        }

        return TicketResponse.builder()
                .id(ticket.getId() != null ? ticket.getId().getValue() : null)
                .ticketCode(ticket.getTicketCode())
                .bookingId(ticket.getBookingId())
                .showtimeSeatId(ticket.getShowtimeSeatId())
                .seatType(ticket.getSeatType())
                .price(ticket.getPrice())
                .status(ticket.getStatus())
                .checkedInAt(ticket.getCheckedInAt())
                .createdAt(ticket.getCreatedAt())
                .build();
    }
}
