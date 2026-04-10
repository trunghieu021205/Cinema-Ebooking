package com.cinemaebooking.backend.movie.domain.enums;

/**
 * MovieStatus: Trạng thái phát hành của phim.
 * @author Hieu Nguyen
 * @since 2026
 */
public enum MovieStatus {

    /**
     * Sắp chiếu
     */
    COMING_SOON,

    /**
     * Đang chiếu
     */
    NOW_SHOWING,

    /**
     * Ngừng chiếu
     */
    ENDED
}