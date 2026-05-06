package com.cinemaebooking.backend.room_layout.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.room.infrastructure.persistence.entity.RoomJpaEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "room_layout")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class RoomLayoutJpaEntity extends BaseJpaEntity {

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "layout_version", nullable = false)
    private Integer layoutVersion;         // Số thứ tự version (1, 2, 3...)

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;       // Ngày bắt đầu có hiệu lực

    @Column(name = "total_rows")
    private Integer totalRows;

    @Column(name = "total_cols")
    private Integer totalCols;

}
