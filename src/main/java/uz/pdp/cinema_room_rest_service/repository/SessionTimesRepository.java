package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.SessionTime;
import uz.pdp.cinema_room_rest_service.projection.SessionTimesProjection;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionTimesRepository extends JpaRepository<SessionTime, UUID> {


    @Query(value = "select\n" +
            "cast(st.id as varchar) as id,\n" +
            "       st.start_time as startTime,\n" +
            "cast(rh.id as varchar) as sessionId\n" +
            "from session_times st\n" +
            "         join movie_sessions rh on st.id = rh.start_time_id\n" +
            "         join halls h on rh.hall_id = h.id\n" +
            "         join movie_announcements ms on rh.movie_announcement_id = ms.id\n" +
            "where ms.id = :sessionId\n" +
            "  and h.id = :hallId" ,nativeQuery = true)
    List<SessionTimesProjection> getTimesBySessionIdAndHallId(UUID hallId, UUID sessionId);

    Optional<SessionTime> findByStartTime(Time startTime);

    @Query(nativeQuery = true,
            value = "select session_additional_fee_in_percentage from session_times\n" +
            "where start_time>:startTime\n" +
            "order by start_time limit 1")
    double findAdditionalFeeByTime(Time startTime);

    @Query(nativeQuery = true,
    value = "select time+:startTime+cast(:startDate as date) from movies where id = :movieId")
    Timestamp findEndTimeByMovieIdAndStartDateTime(UUID movieId, Time startTime, LocalDate startDate);
}
