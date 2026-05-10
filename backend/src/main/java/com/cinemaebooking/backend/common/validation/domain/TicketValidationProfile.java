package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.validation.builder.StringValidationBuilder;
import com.cinemaebooking.backend.common.validation.builder.ValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.ticket.domain.enums.TicketStatus;

import java.util.List;

public class TicketValidationProfile {

    public static final TicketValidationProfile INSTANCE = new TicketValidationProfile();

    private TicketValidationProfile() {}

    /**
     * Quy tắc cho Mã vé (Ticket Code)
     */
    public List<ValidationRule<String>> ticketCodeRules() {
        return StringValidationBuilder.create()
                .notBlank()
                .length(8, 100)
                .build();
    }

    /**
     * Quy tắc cho Số ghế (Seat Number) - VD: A1, B12
     */
    public List<ValidationRule<String>> seatNumberRules() {
        return StringValidationBuilder.create()
                .notBlank()
                .length(2, 10)
                .build();
    }

    /**
     * Quy tắc cho Trạng thái vé
     */
    public List<ValidationRule<TicketStatus>> statusRules() {
        return ValidationBuilder.<TicketStatus>create()
                .notNull()
                .build();
    }
}