package com.cinemaebooking.backend.ticket.application.usecase;

import com.cinemaebooking.backend.common.exception.domain.TicketExceptions;
import com.cinemaebooking.backend.ticket.application.dto.TicketResponse;
import com.cinemaebooking.backend.ticket.application.mapper.TicketResponseMapper;
import com.cinemaebooking.backend.ticket.application.port.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetTicketByCodeUseCase {

    private final TicketRepository ticketRepository;
    private final TicketResponseMapper ticketResponseMapper;

    @Transactional(readOnly = true)
    public TicketResponse execute(String ticketCode) {
        return ticketRepository.findByTicketCode(ticketCode)
                .map(ticketResponseMapper::toTicketResponse)
                .orElseThrow(() -> TicketExceptions.notFound(ticketCode));
    }
}
