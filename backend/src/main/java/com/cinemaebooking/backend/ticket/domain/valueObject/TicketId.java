package com.cinemaebooking.backend.ticket.domain.valueObject;

import com.cinemaebooking.backend.common.domain.BaseId;

public class TicketId extends BaseId {
    public TicketId(Long value){
        super((value));
    }

    public static TicketId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("TicketId value must be positive number");
        }
        return new TicketId(value);
    }

    public static TicketId ofNullable(Long value) {
        return value == null ? null : new TicketId(value);
    }
}
