package com.cinemaebooking.backend.ticket.application.usecase;

import com.cinemaebooking.backend.showtime_seat.application.port.ShowtimeSeatRepository;
import com.cinemaebooking.backend.ticket.application.port.TicketRepository;
import com.cinemaebooking.backend.ticket.domain.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReleaseSeatsUseCase {

    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public void execute(Long showtimeId, List<Ticket> tickets) {
        if (tickets == null || tickets.isEmpty()) return;

        // 1. Lấy danh sách ID của các ghế trong suất chiếu từ danh sách vé
        List<Long> showtimeSeatIds = tickets.stream()
                .map(Ticket::getShowtimeSeatId) // Giả định Ticket có lưu ref tới ShowtimeSeat
                .collect(Collectors.toList());

        // 2. Cập nhật trạng thái ghế trong module Showtime thành AVAILABLE (Trống)
        showtimeSeatRepository.updateStatusToAvailable(showtimeId, showtimeSeatIds);

        // 3. Đánh dấu các vé này là đã hủy (Xóa mềm hoặc đổi status)
        tickets.forEach(ticket -> {
            ticket.cancel();
            ticketRepository.save(ticket);
        });
    }
}