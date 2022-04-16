package uz.pdp.cinema_room_rest_service.projection;

import java.sql.Time;

public interface HallBusyTime {

    Time getStartTime();

    Time getEndTime();
}
