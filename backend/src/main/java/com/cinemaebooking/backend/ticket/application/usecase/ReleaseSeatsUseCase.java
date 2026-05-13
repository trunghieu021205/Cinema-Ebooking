package com.cinemaebooking.backend.ticket.application.usecase;

import com.cinemaebooking.backend.showtime_seat.application.port.ShowtimeSeatRepository;
import com.cinemaebooking.backend.ticket.application.port.TicketRepository;
import com.cinemaebooking.backend.ticket.domain.model.Ticket;
import com.cinemaebooking.backend.ticket.domain.valueObject.TicketId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReleaseSeatsUseCase {

    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public void execute(Long showtimeId, List<Ticket> tickets) {
        if (tickets == null || tickets.isEmpty()) return;

        List<Long> ticketIds = tickets.stream()
                .map(Ticket::getId)
                .map(TicketId::getValue)
                .toList();

        // Load từ DB → JPA entity sẽ có đầy đủ version
        List<Ticket> freshTickets = ticketRepository.findAllByIds(ticketIds);

        List<Long> showtimeSeatIds = freshTickets.stream()
                .map(Ticket::getShowtimeSeatId)
                .toList();

        showtimeSeatRepository.updateStatusToAvailable(showtimeId, showtimeSeatIds);

        freshTickets.forEach(ticket -> {
            ticket.cancel();
            ticketRepository.save(ticket);
        });
    }
}