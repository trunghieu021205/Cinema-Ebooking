package com.cinemaebooking.backend.cinema.application.dto;

import com.cinemaebooking.backend.cinema.domain.enums.CinemaStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * UpdateCinemaRequest: DTO dùng để nhận dữ liệu cập nhật Cinema từ client.
 *
 * <p>Chỉ chứa dữ liệu input, không chứa logic.
 * @author Hieu Nguyen
 * @since 2026
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCinemaRequest {

    /**
     * Tên rạp
     */
    private String name;

    /**
     * Địa chỉ chi tiết
     */
    private String address;

    /**
     * Thành phố
     */
    private String city;

    /**
     * Trạng thái
     */
    private CinemaStatus status;
}
