package com.cinemaebooking.backend.ticket.infrastructure.mapper;

import com.cinemaebooking.backend.booking.infrastructure.persistence.entity.BookingJpaEntity;
import com.cinemaebooking.backend.showtime_seat.infrastructure.persistence.entity.ShowtimeSeatJpaEntity;
import com.cinemaebooking.backend.ticket.domain.model.Ticket;
import com.cinemaebooking.backend.ticket.domain.valueObject.TicketId;
import com.cinemaebooking.backend.ticket.infrastructure.persistence.entity.TicketJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TicketMapperImpl implements TicketMapper {

    @Override
    public Ticket toDomain(TicketJpaEntity e) {
        if (e == null) return null;

        return Ticket.builder()
                .id(TicketId.ofNullable(e.getId()))
                .ticketCode(e.getTicketCode())
                .bookingId(e.getBooking() != null ? e.getBooking().getId() : null)
                .showtimeSeatId(e.getShowtimeSeat() != null ? e.getShowtimeSeat().getId() : null)
                .seatType(e.getSeatType())
                .seatName(e.getSeatName())
                .price(e.getPrice())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .checkedInAt(e.getCheckedInAt())
                .build();
    }

    @Override
    public TicketJpaEntity toEntity(Ticket d) {
        if (d == null) return null;

        return TicketJpaEntity.builder()
                .id(d.getId() != null ? d.getId().getValue() : null)
                .ticketCode(d.getTicketCode())
                .seatType(d.getSeatType())
                .seatName(d.getSeatName())
                .price(d.getPrice())
                .status(d.getStatus())
                .checkedInAt(d.getCheckedInAt())
                .build();
    }
}
