package com.cinemaebooking.backend.cinema.application.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * CreateCinemaRequest: DTO dùng để nhận dữ liệu tạo Cinema từ client.
 *
 * <p>Chỉ chứa dữ liệu input, không chứa logic.
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCinemaRequest {

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
}