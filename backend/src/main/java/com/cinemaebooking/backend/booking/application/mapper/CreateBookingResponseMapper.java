package com.cinemaebooking.backend.booking.application.mapper;

import com.cinemaebooking.backend.booking.application.dto.CreateBookingResponse;
import com.cinemaebooking.backend.booking.domain.model.Booking;
import com.cinemaebooking.backend.ticket.domain.model.Ticket;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class CreateBookingResponseMapper {

    public CreateBookingResponse toCreateResponse(Booking booking) {
        if (booking == null) return null;

        return new CreateBookingResponse(
                booking.getId() != null ? booking.getId().getValue() : null,
                booking.getBookingCode(),
                booking.getTotalTicketPrice(),
                booking.getTotalComboPrice(),
                booking.getDiscountAmount(),
                booking.getFinalAmount(),
                booking.getStatus(),
                booking.getExpiredAt(),
                booking.getTickets() != null
                        ? booking.getTickets().stream().map(Ticket::getShowtimeSeatId).collect(Collectors.toList())
                        : Collections.emptyList()
        );
    }
}
