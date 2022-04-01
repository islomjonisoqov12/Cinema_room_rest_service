package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.Cart;
import uz.pdp.cinema_room_rest_service.projection.CartProjection;
import uz.pdp.cinema_room_rest_service.projection.TicketProjection;

import java.util.List;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Cart findByUserId(UUID userId);

    @Query(nativeQuery = true,
            value = "select cast(t.id as varchar)   as id,\n" +
            "       hr.number               as rowNumber,\n" +
            "       s.number                as seatNumber,\n" +
            "       m.title                 as movieTitle,\n" +
            "       t.price                 as price,\n" +
            "       sd.date + st.start_time as startDate\n" +
            "from tickets t\n" +
            "         join carts c on t.cart_id = c.id\n" +
            "         join users u on c.user_id = u.id\n" +
            "         join movie_sessions ms on ms.id = t.movie_session_id\n" +
            "         join seats s on t.seat_id = s.id\n" +
            "         join movie_announcements ma on ms.movie_announcement_id = ma.id\n" +
            "         join hall_rows hr on s.row_id = hr.id\n" +
            "         join halls h on hr.hall_id = h.id\n" +
            "         join movies m on ma.movie_id = m.id\n" +
            "         join session_dates sd on ms.date_id = sd.id\n" +
            "         join session_times st on ms.start_time_id = st.id\n" +
            "where u.id = :userId and t.status <> 'PURCHASED'")
    List<TicketProjection> getUserCartTickets(UUID userId);



    @Query(nativeQuery = true,
    value = "select sum(t.price)          as totalPrice,\n" +
            "       cast(u.id as varchar) as userId\n" +
            "from tickets t\n" +
            "         join carts c on t.cart_id = c.id\n" +
            "         join users u on c.user_id = u.id\n " +
            "         where u.id = :userId " +
            "group by u.id")
    CartProjection getUserCartById(UUID userId);

    Cart findCartByUserId(UUID userId);
}
