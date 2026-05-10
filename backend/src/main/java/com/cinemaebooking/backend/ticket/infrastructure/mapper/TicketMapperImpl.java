package com.cinemaebooking.backend.ticket.infrastructure.mapper;

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
                .seatNumber(e.getSeatNumber())
                .seatType(e.getSeatType())
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
                .seatNumber(d.getSeatNumber())
                .seatType(d.getSeatType())
                .price(d.getPrice())
                .status(d.getStatus())
                .checkedInAt(d.getCheckedInAt())
                .build();
    }
}
