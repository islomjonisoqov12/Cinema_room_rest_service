package uz.pdp.cinema_room_rest_service.projection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.sql.Date;

public interface HistoryProjection {
    String getId();

    String getStatus();

    LocalDate getDate();

    LocalTime getTime();

    double getTotalAmount();

    String paymentName();

//    @Value("#{@purchaseHistoryRepository.getPayTypeFeeAmount(target.totalAmount, target.id)}")
//    double getPayTypeFeeAmount();

//    @Value("#{@purchaseHistoryRepository.getTickets(target.id)}")
//    List<TicketProjection> tickets();
}
