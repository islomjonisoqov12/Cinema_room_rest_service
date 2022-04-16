package uz.pdp.cinema_room_rest_service.projection;

import java.sql.Time;
import java.time.LocalDate;

public interface MovieProjection {
    String getId();

    String getTitle();

    Time getDuration();

    LocalDate getReleaseDate();

    Double getBudget();

    String getDescription();

}
