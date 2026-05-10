package com.cinemaebooking.backend.booking.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {

    private Long userId;
    private Long showtimeId;
    private List<Long> showTimeSeatIds;
    private String couponCode;
    private List<ComboSelectionItem> combos;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComboSelectionItem {
        private Long comboId;
        private Integer quantity;
    }
}