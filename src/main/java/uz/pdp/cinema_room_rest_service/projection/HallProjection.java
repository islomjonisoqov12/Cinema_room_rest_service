package uz.pdp.cinema_room_rest_service.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import uz.pdp.cinema_room_rest_service.model.Hall;

import java.util.List;
import java.util.UUID;

@Projection(types = Hall.class)
public interface HallProjection {

    String getId();

    String getName();

    boolean getVip();

    @Value("#{@sessionTimesRepository.getTimesBySessionIdAndHallId(target.id,target.sessionId)}")
    List<SessionTimesProjection> getTimes();




}
