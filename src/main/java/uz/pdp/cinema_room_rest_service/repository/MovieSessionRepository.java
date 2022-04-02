package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.AttachmentContent;
import uz.pdp.cinema_room_rest_service.model.MovieSession;

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
}
