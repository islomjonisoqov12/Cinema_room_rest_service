package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.Hall;
import uz.pdp.cinema_room_rest_service.model.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(nativeQuery = true,value = "select email from users\n" +
            "left join purchase_histories ph on users.id = ph.user_id\n" +
            "left join purchase_histories_tickets pht on ph.id = pht.purchase_history_id\n" +
            "where pht.ticket_id = :ticketId limit 1")
    String getEmailByTicketId(UUID ticketId);
}
