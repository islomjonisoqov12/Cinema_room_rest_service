package uz.pdp.cinema_room_rest_service.projection;

import java.time.LocalDateTime;

public interface TicketProjection {
    String getId();

    Integer getRowNumber();

    Integer getSeatNumber();

    String getMovieTitle();

    double getPrice();

    LocalDateTime getStartDate();




}
