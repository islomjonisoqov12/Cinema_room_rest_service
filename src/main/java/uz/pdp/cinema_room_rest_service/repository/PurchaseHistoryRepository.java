package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.PurchaseHistory;
import uz.pdp.cinema_room_rest_service.projection.HistoryByIdProjection;
import uz.pdp.cinema_room_rest_service.projection.HistoryProjection;
import uz.pdp.cinema_room_rest_service.projection.TicketProjection;

import java.util.List;
import java.util.UUID;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, UUID> {
    @Query(nativeQuery = true,
            value = "select\n" +
                    "    cast(ph.id as varchar) as id,\n" +
                    "    (case when ph.refund then 'REFUNDED'\n" +
                    "          else 'PAYED' end) as status ,\n" +
                    "    cast(ph.created_at as date) as date,\n" +
                    "    cast(ph.created_at as time) as time,\n" +
                    "    ph.total_amount as totalAmount,\n" +
                    "    pt.name as paymentName\n" +
                    "from purchase_histories ph\n" +
                    "         left join pay_types pt on ph.pay_type_id = pt.id\n" +
                    "         left join users u on u.id = ph.user_id\n" +
                    "where u.username = :userName and cast(ph.created_at as varchar) like :search"
    )
    Page<HistoryProjection> findUserHistoryByUserName(String userName, String search, Pageable pageable);

    @Query(nativeQuery = true,value = "select cast(t.id as varchar)   as id,\n" +
            "       hr.number               as rowNumber,\n" +
            "       s.number                as seatNumber,\n" +
            "       m.title                 as movieTitle,\n" +
            "       t.price                 as price,\n" +
            "       sd.date + st.start_time as startDate\n" +
            "from tickets t\n" +
            "         left join carts c on t.cart_id = c.id\n" +
            "         left join users u on c.user_id = u.id\n" +
            "         left join movie_sessions ms on ms.id = t.movie_session_id\n" +
            "         left join seats s on t.seat_id = s.id\n" +
            "         left join movie_announcements ma on ms.movie_announcement_id = ma.id\n" +
            "         left join hall_rows hr on s.row_id = hr.id\n" +
            "         left join halls h on hr.hall_id = h.id\n" +
            "         left join movies m on ma.movie_id = m.id\n" +
            "         left join session_dates sd on ms.date_id = sd.id\n" +
            "         left join session_times st on ms.start_time_id = st.id\n" +
            "         left join purchase_histories_tickets pht on t.id = pht.ticket_id\n" +
            "         left join purchase_histories ph on ph.id = pht.purchase_history_id\n" +
            "where ph.id = :purchaseHistoryId")
    List<TicketProjection> getTickets(UUID purchaseHistoryId);


    @Query(nativeQuery = true,value =
            "select ph.total_amount* (pt.fee_in_per/100)+pt.fee_in_amount  from purchase_histories ph\n" +
            "join pay_types pt on ph.pay_type_id = pt.id")
    double getPayTypeFeeAmount(UUID historyId);


    @Query(nativeQuery = true,value = "select cast(ph.id as varchar)      as id,\n" +
            "       (case\n" +
            "            when ph.refund then 'REFUNDED'\n" +
            "            else 'PAYED' end)      as status,\n" +
            "       cast(ph.created_at as date) as date,\n" +
            "       cast(ph.created_at as time) as time,\n" +
            "       ph.total_amount             as totalAmount,\n" +
            "       pt.name                     as paymentName\n" +
            "from purchase_histories ph\n" +
            "         join purchase_histories_tickets pht on ph.id = pht.purchase_history_id\n" +
            "         join tickets t on pht.ticket_id = t.id\n" +
            "         join pay_types pt on ph.pay_type_id = pt.id;\n")
    HistoryByIdProjection findByHistoryId(UUID historyId);
}
