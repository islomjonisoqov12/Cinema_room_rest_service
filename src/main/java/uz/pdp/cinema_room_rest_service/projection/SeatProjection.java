package uz.pdp.cinema_room_rest_service.projection;

public interface SeatProjection {

    String getId();
    String getMovieSessionId();
    boolean getAvailable();
    String getHallName();
    String getHallId();
    Integer getSeatNumber();
    Integer getRowNumber();
    String getPriceCategoryName();
    String getPriceCategoryId();


}
