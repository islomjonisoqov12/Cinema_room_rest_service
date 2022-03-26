package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.MovieAnnouncement;
import uz.pdp.cinema_room_rest_service.projection.MovieSessionProjection;
import uz.pdp.cinema_room_rest_service.projection.SessionProjection;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MovieAnnouncementRepository extends JpaRepository<MovieAnnouncement, UUID> {

    @Query(nativeQuery = true, value =
            "select cast(m.id as varchar) as id," +
                    " m.time as title" +
                    "  from movies m ")
    List<MovieAnnouncement> findBYMovieId(UUID movieId);

    @Query(value = "select\n" +
            "            cast(ms.id as varchar) as id,\n" +
            "            cast(m.id as varchar) as movieId,\n" +
            "            m.title as movieTitle, " +
            "            cast(a.id as varchar) as imgId, " +
            "            cast(a2.id as varchar) as videoId " +
            "            from movie_announcements ms\n" +
            "            left join movies m on m.id = ms.movie_id\n" +
            "            left join movie_sessions rh on ms.id = rh.movie_announcement_id\n" +
            "            left join session_dates sd on rh.date_id = sd.id\n" +
            "            left join session_times st on rh.start_time_id = st.id\n " +
            "            left join attachments a on m.poster_img = a.id " +
            "            left join attachments a2 on a2.id = m.trailer_video " +
            "            group by ms.id, m.id,m.title, a.id, a2.id"
            , nativeQuery = true)
    List<SessionProjection> getAllSession(PageRequest pageable);


    @Query(value = "select " +
            "       cast(ms.id as varchar) as id,\n" +
            "       cast(m.id as varchar)  as movieId,\n" +
            "       m.title                as movieTitle,\n" +
            "       cast(a.id as varchar)  as imgId,\n" +
            "       cast(a2.id as varchar) as videoId\n" +
            "from movie_announcements ms\n" +
            "         left join movies m on m.id = ms.movie_id\n" +
            "         left join movie_sessions rh on ms.id = rh.movie_announcement_id\n" +
            "         left join session_dates sd on rh.date_id = sd.id\n" +
            "         left join session_times st on rh.start_time_id = st.id\n" +
            "         left join attachments a on m.poster_img = a.id\n" +
            "         left join attachments a2 on a2.id = m.trailer_video\n" +
            "         left join movie_sessions rs on rs.movie_announcement_id = ms.id\n" +
            "         join halls h on rh.hall_id = h.id\n" +
            "where ms.id = :id\n" +
            "group by ms.id, m.title, m.id, a.id, a2.id", nativeQuery = true)
    MovieSessionProjection getSessionById(UUID id);


    @Query(value = "select get_min_date_of_announcement(:session_id)", nativeQuery = true)
    LocalDate getStartDate(UUID session_id);

    @Query(value = "select get_max_date_of_announcement(:session_id)", nativeQuery = true)
    LocalDate getEndDate(UUID session_id);
}
