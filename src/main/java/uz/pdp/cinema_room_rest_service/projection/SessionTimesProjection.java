package uz.pdp.cinema_room_rest_service.projection;

import java.time.LocalTime;

public interface SessionTimesProjection {

    String getId();

    LocalTime getStartTime();

    String getSessionId();
}
