package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.AttachmentContent;
import uz.pdp.cinema_room_rest_service.model.MovieSession;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MovieSessionRepository extends JpaRepository<MovieSession, UUID> {


    @Query(nativeQuery = true,
            value = "select ac.data\n" +
                    "from tickets\n" +
                    "         left join movie_sessions ms on tickets.movie_session_id = ms.id\n" +
                    "         left join movie_announcements ma on ms.movie_announcement_id = ma.id\n" +
                    "         left join movies m on ma.movie_id = m.id\n" +
                    "         left join attachments a on m.poster_img = a.id\n" +
                    "         left join attachment_contents ac on a.id = ac.attachment_id\n" +
                    "where tickets.id = :ticketId")
    byte[] getAttachmentByTicketId(UUID ticketId);


    @Query(nativeQuery = true,value =
            "select cast(ms.id as varchar) as                                                      id,\n" +
            "       h.name                 as                                                      hallName,\n" +
            "       m.title                as                                                      movieTitle,\n" +
            "       count(t.id)            as                                                      ticketCount,\n" +
            "       sd.date                as                                                      startDate,\n" +
            "       get_available_seat_count(ms.id) * 100 / get_available_seat_count(ms.id, false) busySeatsPersentage\n" +
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
    List<Map<String , Object>> getTopSession(int size, boolean expired);


}
