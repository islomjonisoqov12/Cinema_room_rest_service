package uz.pdp.cinema_room_rest_service.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import uz.pdp.cinema_room_rest_service.model.SessionDates;

import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface SessionDateProjection {

    String getId();

    LocalDate getDate();


    @Value("#{@hallRepository.getHallsBySessionId(target.id, target.sessionId)}")
    List<HallProjection> getHalls();


}
