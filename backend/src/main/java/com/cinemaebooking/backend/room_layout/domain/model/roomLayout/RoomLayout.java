package com.cinemaebooking.backend.room_layout.domain.model.roomLayout;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.room.domain.enums.RoomType;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayoutSeat.RoomLayoutSeat;
import com.cinemaebooking.backend.room_layout.domain.valueObject.roomLayout.RoomLayoutId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@SuperBuilder(toBuilder = true)
public class RoomLayout extends BaseEntity<RoomLayoutId> {

    private Long roomId;

    private Integer layoutVersion;

    @Setter
    private RoomType roomType;
    @Setter
    private LocalDate effectiveDate;

    private Integer totalRows;

    private Integer totalCols;

    private List<RoomLayoutSeat> seats;

    private LocalDateTime createdAt;

    private RoomLayout(RoomLayoutBuilder<?, ?> builder) {
        super(builder);
        this.roomId = builder.roomId;
        this.layoutVersion = builder.layoutVersion;
        this.roomType = builder.roomType;
        this.effectiveDate = builder.effectiveDate;
        this.totalRows = builder.totalRows;
        this.totalCols = builder.totalCols;
        this.createdAt = builder.createdAt;
        this.seats = builder.seats != null ? List.copyOf(builder.seats) : null;
        validate();
    }

    public static RoomLayoutBuilder<?, ?> builder() {
        return new RoomLayoutBuilderImpl();
    }

    private void validate() {
        if (roomId == null) {
            throw CommonExceptions.invalidInput("roomId", ErrorCategory.REQUIRED, "roomId cannot be null");
        }
        if (layoutVersion == null || layoutVersion <= 0) {
            throw CommonExceptions.invalidInput("layoutVersion", ErrorCategory.INVALID_VALUE, "layoutVersion must be > 0");
        }
        if (roomType == null) {
            throw CommonExceptions.invalidInput("roomType", ErrorCategory.REQUIRED, "roomType cannot be null");
        }
        if (effectiveDate == null) {
            throw CommonExceptions.invalidInput("effectiveDate", ErrorCategory.REQUIRED, "effectiveDate cannot be null");
        }
        if (totalRows == null || totalRows <= 0) {
            throw CommonExceptions.invalidInput("totalRows", ErrorCategory.INVALID_VALUE, "totalRows must be > 0");
        }
        if (totalCols == null || totalCols <= 0) {
            throw CommonExceptions.invalidInput("totalCols", ErrorCategory.INVALID_VALUE, "totalCols must be > 0");
        }
        if (seats != null) {
            if (seats.isEmpty()) {
                throw CommonExceptions.invalidInput("seats", ErrorCategory.REQUIRED, "seats cannot be empty");
            }
            int expectedSize = totalRows * totalCols;
            if (seats.size() != expectedSize) {
                throw CommonExceptions.invalidInput("seats", ErrorCategory.INVALID_VALUE,
                        "Number of seats must equal totalRows * totalCols. Expected: " + expectedSize + ", but was: " + seats.size());
            }
            Set<String> occupied = new HashSet<>();
            for (RoomLayoutSeat seat : seats) {
                if (seat.getRowIndex() == null || seat.getRowIndex() < 1 || seat.getRowIndex() > totalRows) {
                    throw CommonExceptions.invalidInput("seats", ErrorCategory.INVALID_VALUE,
                            "Invalid row index: " + seat.getRowIndex());
                }
                if (seat.getColIndex() == null || seat.getColIndex() < 1 || seat.getColIndex() > totalCols) {
                    throw CommonExceptions.invalidInput("seats", ErrorCategory.INVALID_VALUE,
                            "Invalid col index: " + seat.getColIndex());
                }
                String key = seat.getRowIndex() + "," + seat.getColIndex();
                if (occupied.contains(key)) {
                    throw CommonExceptions.invalidInput("seats", ErrorCategory.INVALID_VALUE,
                            "Duplicate seat at row " + seat.getRowIndex() + ", col " + seat.getColIndex());
                }
                occupied.add(key);
            }
        }
    }
}
