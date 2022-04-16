package uz.pdp.cinema_room_rest_service.projection;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;


public interface CartProjection {

    double getTotalPrice();

    @Value("#{@cartRepository.getUserCartTickets(target.userId)}")
    List<TicketProjection> getTickets();
}
