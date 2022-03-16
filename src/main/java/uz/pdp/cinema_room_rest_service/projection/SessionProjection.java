package uz.pdp.cinema_room_rest_service.projection;

import uz.pdp.cinema_room_rest_service.model.Hall;
import uz.pdp.cinema_room_rest_service.model.Movie;

import java.sql.Time;
import java.util.Date;

public interface SessionProjection {

    String getId();

    Date getDate();

    Time getTime();

    Movie getMovie();

    Hall getHall();

}
