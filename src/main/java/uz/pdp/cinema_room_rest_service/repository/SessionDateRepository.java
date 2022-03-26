package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.SessionDates;
import uz.pdp.cinema_room_rest_service.projection.SessionDateProjection;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SessionDateRepository extends JpaRepository<SessionDates, UUID> {

    @Query(value = "select cast(sd.id as varchar) as id,\n" +
            "       sd.date as date,\n" +
            "       cast(movie_announcements.id as varchar) as sessionId\n" +
            "from movie_announcements\n" +
            "         join movie_sessions rh on movie_announcements.id = rh.movie_announcement_id\n" +
            "         join session_dates sd on rh.date_id = sd.id\n" +
            "where movie_announcements.id = :sessionId",nativeQuery = true)
    List<SessionDateProjection> getSessionDateAnd(UUID sessionId);

    SessionDates findByDate(LocalDate date);
}
