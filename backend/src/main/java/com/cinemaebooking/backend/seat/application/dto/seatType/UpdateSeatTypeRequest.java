package com.cinemaebooking.backend.seat.application.dto.seatType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSeatTypeRequest {

    @NotBlank(message = "Seat type name must not be blank")
    private String name;

    @NotNull(message = "Base price must not be null")
    @Min(value = 1, message = "Base price must be at least 1")
    private Long basePrice;
}