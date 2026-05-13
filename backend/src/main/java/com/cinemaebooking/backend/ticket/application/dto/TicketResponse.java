package com.cinemaebooking.backend.ticket.application.dto;

import com.cinemaebooking.backend.ticket.domain.enums.TicketStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private String ticketCode;
    private Long bookingId;
    private Long showtimeSeatId;
    private String seatNumber;
    private String seatType;
    private BigDecimal price;
    private TicketStatus status;
    private LocalDateTime checkedInAt;
    private LocalDateTime createdAt;
}