package uz.pdp.cinema_room_rest_service.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface HistoryByIdProjection {
    String getId();

    String getStatus();

    LocalDate getDate();

    LocalTime getTime();

    double getTotalAmount();

    String paymentName();

    @Value("#{@purchaseHistoryRepository.getPayTypeFeeAmount(target.id)}")
    double getPayTypeFeeAmount();

    @Value("#{@purchaseHistoryRepository.getTickets(target.id)}")
    List<TicketProjection> tickets();
}
