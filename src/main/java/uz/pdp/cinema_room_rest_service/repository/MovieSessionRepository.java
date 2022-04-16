package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.MovieSession;
import uz.pdp.cinema_room_rest_service.model.Seat;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface MovieSessionRepository extends JpaRepository<MovieSession, UUID> {


    @Query(nativeQuery = true,
            value = "select ac.data\n" +
                    "from tickets\n" +
                    "         left join movie_sessions ms on tickets.movie_session_id = ms.id\n" +
                    "         left join movie_announcements ma on ms.movie_announcement_id = ma.id\n" +
                    "         left join movies m on ma.movie_id = m.id\n" +
                    "         left join movies_poster_img mpi on m.id = mpi.movies_id\n" +
                    "         left join attachments a on mpi.poster_img_id = a.id\n" +
                    "         left join attachment_contents ac on a.id = ac.attachment_id\n" +
                    "where tickets.id = :ticketId limit 1")
    byte[] getAttachmentByTicketId(UUID ticketId);



    @Query(nativeQuery = true,value =
            "select cast(ms.id as varchar)                                       as id,\n" +
                    "       h.name                                                       as \"hallName\",\n" +
                    "       m.title                                                      as \"movieTitle\",\n" +
                    "       count(distinct t.id)                                                  as \"ticketCount\",\n" +
                    "       sd.date                                                      as \"startDate\",\n" +
                    "       get_seat_count(ms.id)                    as \"availableSeatCount\",\n" +
                    "       get_seat_count(ms.id, 'ALL')  as \"seatCount\",\n" +
                    "       get_seat_count(ms.id) * 100 /\n" +
                    "       get_seat_count(ms.id, 'ALL')  as \"availableSeatPercentage\"\n" +
                    "from movie_sessions ms\n" +
                    "         left join movie_announcements ma on ma.id = ms.movie_announcement_id\n" +
                    "         left join movies m on m.id = ma.movie_id\n" +
                    "         left join tickets t on ms.id = t.movie_session_id\n" +
                    "         left join halls h on h.id = ms.hall_id\n" +
                    "         left join session_dates sd on ms.date_id = sd.id\n" +
                    "         left join session_times st on ms.start_time_id = st.id\n" +
                    "where (sd.date + st.start_time > now() or (:expired = true))\n" +
                    "group by ms.id, h.name, m.title, sd.date\n" +
                    "order by count(t.id) desc\n" +
                    "limit :size")
//    @Query(nativeQuery = true,
//            value = "select h.name," +
//                    "cast(json_agg(json_build_object('rowNumber', hr.number)) as text) as rows from halls h\n" +
//            "join hall_rows hr on h.id = hr.hall_id\n" +
//            "group by h.name")
    List<Map<String , Object>>getTopSession(int size, boolean expired);


    @Query(nativeQuery = true,value = "select title as \"movieTitle\",\n" +
            "       sd.date as date,\n" +
            "       st.start_time as \"startTime\",\n" +
            "       end_time as \"endTime\"\n" +
            "from movie_sessions ms\n" +
            "         join session_times st on ms.start_time_id = st.id\n" +
            "         join session_dates sd on ms.date_id = sd.id\n" +
            "         join halls h on ms.hall_id = h.id\n" +
            "         join movie_announcements ma on ms.movie_announcement_id = ma.id\n" +
            "         join movies m on ma.movie_id = m.id\n" +
            "where (sd.date = :date and\n" +
            "       (st.start_time <= :startTime and ms.end_time >= :endTime --///            start ---- end\n" +
            "           or st.start_time <= :startTime and end_time >= :startTime --///            start ---- end -----\n" +
            "           or st.start_time >= :startTime and st.start_time < :endTime --///            ---- start ---- end\n" +
            "           or st.start_time >= :startTime and end_time <= :endTime)) --///            ---- start ---- end ----\n" +
            "  and h.id = :hallId")
    Map<String, Object> hasAnySession(LocalDate date, Time startTime, Time endTime, UUID hallId);

    @Query(value = "select cast(movie_sessions.id as varchar) as id\n" +
            "    from movie_sessions\n" +
            "             join session_dates sd on movie_sessions.date_id = sd.id\n" +
            "             join session_times st on movie_sessions.start_time_id = st.id\n" +
            "    where (sd.date + st.start_time) between :start and :end", nativeQuery = true)
    List<UUID> deleteByDateTime(Timestamp start, Timestamp end);
}
