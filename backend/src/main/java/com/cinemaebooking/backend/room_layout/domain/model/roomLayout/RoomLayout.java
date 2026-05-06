package com.cinemaebooking.backend.room_layout.domain.model.roomLayout;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayoutSeat.RoomLayoutSeat;
import com.cinemaebooking.backend.room_layout.domain.valueObject.roomLayout.RoomLayoutId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@SuperBuilder(toBuilder = true)
public class RoomLayout extends BaseEntity<RoomLayoutId> {

    private Long roomId;

    private Integer layoutVersion;

    private LocalDate effectiveDate;

    private Integer totalRows;

    private Integer totalCols;

    private List<RoomLayoutSeat> seats;

    private RoomLayout(RoomLayoutBuilder<?, ?> builder) {
        super(builder);
        this.roomId = builder.roomId;
        this.layoutVersion = builder.layoutVersion;
        this.effectiveDate = builder.effectiveDate;
        this.totalRows = builder.totalRows;
        this.totalCols = builder.totalCols;
        this.seats = builder.seats != null ? List.copyOf(builder.seats) : null;
        validate();
    }

    public static RoomLayoutBuilder<?, ?> builder() {
        return new RoomLayoutBuilderImpl();
    }

    private void validate() {
        if (roomId == null) throw new IllegalArgumentException("roomId cannot be null");
        if (layoutVersion == null || layoutVersion <= 0) throw new IllegalArgumentException("layoutVersion must be > 0");
        if (effectiveDate == null) throw new IllegalArgumentException("effectiveDate cannot be null");
        if (totalRows == null || totalRows <= 0) throw new IllegalArgumentException("totalRows must be > 0");
        if (totalCols == null || totalCols <= 0) throw new IllegalArgumentException("totalCols must be > 0");
        if (seats == null || seats.isEmpty()) throw new IllegalArgumentException("seats cannot be empty");

        // Kiểm tra số lượng ghế
        int expectedSize = totalRows * totalCols;
        if (seats.size() != expectedSize) {
            throw new IllegalArgumentException("Number of seats must equal totalRows * totalCols. Expected: " + expectedSize + ", but was: " + seats.size());
        }

        // Kiểm tra chỉ số hàng/cột và trùng lặp
        Set<String> occupied = new HashSet<>();
        for (RoomLayoutSeat seat : seats) {
            if (seat.getRowIndex() == null || seat.getRowIndex() < 1 || seat.getRowIndex() > totalRows) {
                throw new IllegalArgumentException("Invalid row index: " + seat.getRowIndex());
            }
            if (seat.getColIndex() == null || seat.getColIndex() < 1 || seat.getColIndex() > totalCols) {
                throw new IllegalArgumentException("Invalid col index: " + seat.getColIndex());
            }
            String key = seat.getRowIndex() + "," + seat.getColIndex();
            if (occupied.contains(key)) {
                throw new IllegalArgumentException("Duplicate seat at row " + seat.getRowIndex() + ", col " + seat.getColIndex());
            }
            occupied.add(key);
        }
    }
}
