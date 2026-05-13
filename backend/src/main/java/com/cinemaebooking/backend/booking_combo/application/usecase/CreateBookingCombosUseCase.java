package com.cinemaebooking.backend.booking_combo.application.usecase;

import com.cinemaebooking.backend.booking_combo.application.dto.CreateBookingComboRequest;
import com.cinemaebooking.backend.booking_combo.application.port.BookingComboRepository;
import com.cinemaebooking.backend.booking_combo.domain.model.BookingCombo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateBookingCombosUseCase {

    private final BookingComboRepository bookingComboRepository;

    @Transactional
    public void execute(Long bookingId, List<CreateBookingComboRequest> requests) {
        if (requests == null || requests.isEmpty()) return;

        requests.forEach(req -> {
            BookingCombo combo = BookingCombo.builder()
                    .bookingId(bookingId)
                    .comboId(req.getComboId())
                    .comboName(req.getComboName())
                    .unitPrice(req.getUnitPrice())
                    .quantity(req.getQuantity())
                    .build();

            combo.calculateTotalPrice();
            bookingComboRepository.save(combo);
        });
    }
}
