package uz.pdp.cinema_room_rest_service.projection;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Date;
import java.util.List;

public interface HallBusyDate {

    Date getDate();

    @Value("#{@hallRepository.getBusyTimeByDateAndHallId(target.date, target.id)}")
    List<HallBusyTime> getTimes();
}
