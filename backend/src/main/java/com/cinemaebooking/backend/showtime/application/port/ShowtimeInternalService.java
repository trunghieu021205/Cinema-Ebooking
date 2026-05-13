package com.cinemaebooking.backend.showtime.application.port;

import com.cinemaebooking.backend.showtime.application.dto.showtime.ShowtimeSnapshot;
import com.cinemaebooking.backend.ticket.domain.model.Ticket;

import java.util.List;

public interface ShowtimeInternalService {
    ShowtimeSnapshot getSnapshot(Long showtimeId);
    List<Ticket> getTicketsBySeatIds(Long showtimeId, List<Long> seatIds);
}
