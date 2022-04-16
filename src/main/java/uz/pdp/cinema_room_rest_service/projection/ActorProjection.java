package uz.pdp.cinema_room_rest_service.projection;

import java.time.LocalDate;
import java.sql.Date;

public interface ActorProjection {

    String getId();

    String getFullName();

    LocalDate getDateOfBirth();

    String getBio();

}
