package com.cinemaebooking.backend.movie.domain.enums;

import lombok.Getter;

/**
 * AgeRating: Phân loại độ tuổi của phim.
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
public enum AgeRating {

    P("All Ages"),
    T13("13+"),
    T16("16+"),
    T18("18+");

    private final String label;

    AgeRating(String label) {
        this.label = label;
    }
}