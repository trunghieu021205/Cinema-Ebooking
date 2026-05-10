package com.cinemaebooking.backend.ticket.application.usecase;

import com.cinemaebooking.backend.ticket.application.dto.CreateTicketRequest;
import com.cinemaebooking.backend.ticket.application.port.TicketRepository;
import com.cinemaebooking.backend.ticket.application.validator.TicketCommandValidator;
import com.cinemaebooking.backend.ticket.domain.enums.TicketStatus;
import com.cinemaebooking.backend.ticket.domain.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateTicketsUseCase {

    private final TicketRepository ticketRepository;
    private final TicketCommandValidator ticketCommandValidator;

    @Transactional
    public void execute(Long bookingId, List<CreateTicketRequest> requests) {
        // 1. Validate dữ liệu đầu vào
        ticketCommandValidator.validateCreateRequests(requests);

        // 2. Tạo vé cho từng ghế
        requests.forEach(req -> {
            Ticket ticket = Ticket.builder()
                    .bookingId(bookingId)
                    .showtimeSeatId(req.getShowtimeSeatId())
                    .seatNumber(req.getSeatNumber())
                    .seatType(req.getSeatType())
                    .price(req.getPrice())
                    .status(TicketStatus.ACTIVE)
                    .ticketCode(Ticket.generateTicketCode())
                    .build();

            ticketRepository.save(ticket);
        });
    }
}