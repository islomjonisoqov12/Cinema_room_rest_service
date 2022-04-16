package uz.pdp.cinema_room_rest_service.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import uz.pdp.cinema_room_rest_service.model.MovieAnnouncement;

import java.util.List;

@Projection(types = MovieAnnouncement.class)
public interface MovieSessionProjection {

    String getId();

    String getMovieId();

    String getMovieTitle();

    String getImgId();

    String getVideoId();

    @Value("#{@sessionDateRepository.getSessionDateAnd(target.id)}")
    List<SessionDateProjection> getSessions();

}
