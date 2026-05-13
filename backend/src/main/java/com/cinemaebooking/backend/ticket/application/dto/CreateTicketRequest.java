package com.cinemaebooking.backend.ticket.application.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketRequest {
    private Long showtimeSeatId;
    private String seatNumber;
    private String seatType;
    private BigDecimal price;
}
