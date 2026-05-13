package com.cinemaebooking.backend.ticket.application.validator;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.ticket.application.dto.CheckInTicketRequest;
import com.cinemaebooking.backend.ticket.application.dto.CreateTicketRequest;
import com.cinemaebooking.backend.ticket.application.port.TicketRepository;
import org.springframework.stereotype.Component;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TicketCommandValidator {

    private final TicketRepository ticketRepository;

    public void validateCreateRequests(List<CreateTicketRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw CommonExceptions.invalidInput("Danh sách vé tạo mới không được để trống");
        }

        ValidationEngine engine = ValidationEngine.of();

        for (int i = 0; i < requests.size(); i++) {
            CreateTicketRequest req = requests.get(i);
            String prefix = "tickets[" + i + "].";

            engine.validate(req.getSeatNumber(), prefix + "seatNumber", ValidationFactory.ticket().seatNumberRules());
        }

        engine.throwIfInvalid();
    }

    /**
     * Validate khi thực hiện Check-in
     */
    public void validateCheckInRequest(CheckInTicketRequest request) {
        if (request == null) {
            throw CommonExceptions.invalidInput("Yêu cầu check-in không được để trống");
        }

        // ================== PHASE 1: FORMAT VALIDATION ==================
        ValidationEngine engine = ValidationEngine.of()
                .validate(request.getTicketCode(), "ticketCode", ValidationFactory.ticket().ticketCodeRules());

        if (engine.hasErrors()) {
            engine.throwIfInvalid();
            return;
        }

        engine.validateUnique(request.getTicketCode(), "ticketCode",
                value -> !ticketRepository.findByTicketCode(value).isPresent());

        engine.throwIfInvalid();
    }
}
