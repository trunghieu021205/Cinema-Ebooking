package com.cinemaebooking.backend.cinema.domain.model;

import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.cinema.domain.enums.CinemaStatus;
import com.cinemaebooking.backend.common.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


/**
 * Cinema: Aggregate Root đại diện cho rạp chiếu phim.
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Quản lý thông tin rạp (name, location)</li>
 *     <li>Quan hệ với Room (1-N)</li>
 * </ul>
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class Cinema extends BaseEntity<CinemaId> {

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
