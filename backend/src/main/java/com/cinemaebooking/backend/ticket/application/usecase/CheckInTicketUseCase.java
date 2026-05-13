package com.cinemaebooking.backend.ticket.application.usecase;

import com.cinemaebooking.backend.common.exception.domain.TicketExceptions;
import com.cinemaebooking.backend.ticket.application.dto.CheckInTicketRequest;
import com.cinemaebooking.backend.ticket.application.dto.TicketResponse;
import com.cinemaebooking.backend.ticket.application.mapper.TicketResponseMapper;
import com.cinemaebooking.backend.ticket.application.port.TicketRepository;
import com.cinemaebooking.backend.ticket.application.validator.TicketCommandValidator;
import com.cinemaebooking.backend.ticket.domain.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckInTicketUseCase {

    private final TicketRepository ticketRepository;
    private final TicketResponseMapper ticketResponseMapper;
    private final TicketCommandValidator ticketCommandValidator;

    @Transactional
    public TicketResponse execute(CheckInTicketRequest request) {

        ticketCommandValidator.validateCheckInRequest(request);

        Ticket ticket = ticketRepository.findByTicketCode(request.getTicketCode())
                .orElseThrow(() -> TicketExceptions.notFound(request.getTicketCode()));

        ticket.checkIn();

        return ticketResponseMapper.toTicketResponse(ticketRepository.save(ticket));
    }
}