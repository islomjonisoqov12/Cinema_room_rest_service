package uz.pdp.cinema_room_rest_service.projection;


import org.springframework.data.rest.core.config.Projection;
import uz.pdp.cinema_room_rest_service.model.Distributor;

public interface DistributeProjection {

    String getId();

    String getName();

    String getDescription();

    String getImgId();
}
