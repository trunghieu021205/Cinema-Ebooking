package com.cinemaebooking.backend.cinema.infrastructure.persistence.entity;

import com.cinemaebooking.backend.cinema.domain.enums.CinemaStatus;
import com.cinemaebooking.backend.common.domain.BaseId;
import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


/**
 * CinemaJpaEntity: Mapping JPA cho Cinema domain entity.
 *
 * <p>
 * Chịu trách nhiệm:
 * <ul>
 *     <li>Mapping bảng "cinemas"</li>
 *     <li>Mapping quan hệ với Room (1-N)</li>
 *     <li>Kế thừa auditing + soft delete từ BaseJpaEntity</li>
 * </ul>
 *
 * <p>
 * Lưu ý:
 * <ul>
 *     <li>Domain dùng CinemaId, DB dùng idJpa</li>
 *     <li>rooms được map lazy để tránh load dư</li>
 * </ul>
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(name = "cinemas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class CinemaJpaEntity extends BaseJpaEntity {

    /**
     * Tên rạp
     */
    @Column(nullable = false, length = 255)
    private String name;

    /**
     * Địa chỉ chi tiết
     */
    @Column(nullable = false, length = 500)
    private String address;

    /**
     * Thành phố
     */
    @Column(nullable = false, length = 100)
    private String city;

    /**
     * Trạng thái hoạt động
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CinemaStatus status;

}
