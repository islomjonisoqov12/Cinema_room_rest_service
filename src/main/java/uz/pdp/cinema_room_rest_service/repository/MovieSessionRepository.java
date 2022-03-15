package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.Hall;
import uz.pdp.cinema_room_rest_service.model.MovieSession;
import uz.pdp.cinema_room_rest_service.projection.SessionProjection;

import java.util.List;
import java.util.UUID;

public interface MovieSessionRepository extends JpaRepository<MovieSession, UUID> {

    @Query(nativeQuery = true,value =
            "select cast(m.id as varchar) as id," +
                    " m.time as title" +
                    "  from movies m ")
    List<MovieSession> findBYMovieId(UUID movieId);

    @Query("select ms.id,ms.date,ms.time, ms.movie, ms.hall from movie_sessions ms")
    List<SessionProjection> getAllSession();
}
