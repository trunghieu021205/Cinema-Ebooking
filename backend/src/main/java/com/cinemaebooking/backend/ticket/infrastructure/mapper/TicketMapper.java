package com.cinemaebooking.backend.ticket.infrastructure.mapper;

import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;
import com.cinemaebooking.backend.ticket.domain.model.Ticket;
import com.cinemaebooking.backend.ticket.infrastructure.persistence.entity.TicketJpaEntity;

public interface TicketMapper extends BaseMapper<Ticket, TicketJpaEntity> {
}
