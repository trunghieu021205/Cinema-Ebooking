package com.cinemaebooking.backend.cinema.application.dto;

import com.cinemaebooking.backend.cinema.domain.enums.CinemaStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CinemaResponse: DTO dùng để trả dữ liệu Cinema về client.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Expose thông tin Cinema ra ngoài API</li>
 *     <li>Không expose trực tiếp Domain Model hoặc JPA Entity</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Chỉ chứa dữ liệu output, không chứa business logic</li>
 *     <li>Dùng để đảm bảo API response ổn định khi domain thay đổi</li>
 * </ul>
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CinemaResponse {

    /**
     * ID của Cinema
     */
    private Long id;

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
     * Trạng thái hoạt động của rạp
     */
    private CinemaStatus status;

}