package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.Ticket;
import uz.pdp.cinema_room_rest_service.projection.TicketProjection;

import java.util.Date;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @Query(value = "select  count(distinct t.id)>0 from movie_sessions\n" +
            "join halls h on movie_sessions.hall_id = h.id\n" +
            "join hall_rows hr on h.id = hr.hall_id\n" +
            "join seats s on hr.id = s.row_id\n" +
            "join tickets t on movie_sessions.id = t.movie_session_id and s.id = t.seat_id\n" +
            "where movie_sessions.id = :sessionId and s.id = :seatId and t.status <> 'REFUNDED'", nativeQuery = true)
    boolean existsBySeatIdAndMovieSessionId(UUID seatId, UUID sessionId);

    @Query(nativeQuery = true, value = "select " +
            "m.price +\n" +
            "       m.price * pc.additional_fee_in_percentage/100 +\n" +
            "       st.session_additional_fee_in_percentage * m.price/100 +" +
            "       h.vip_additional_fee_in_percentage * m.price/100  \n" +
            "          as ticketPrice\n" +
            "from movie_sessions\n" +
            "         join halls h on movie_sessions.hall_id = h.id\n" +
            "         join hall_rows hr on h.id = hr.hall_id\n" +
            "         join seats s on hr.id = s.row_id\n" +
            "         join price_categories pc on s.price_category_id = pc.id\n" +
            "         join movie_announcements ma on movie_sessions.movie_announcement_id = ma.id\n" +
            "         join movies m on ma.movie_id = m.id\n" +
            "         join session_times st on movie_sessions.start_time_id = st.id\n" +
            "where movie_sessions.id = ?2 and s.id = ?1")
    Double getPriceFromSeatIdAndSessionId(UUID seatId, UUID sessionId);

    @Query(nativeQuery = true, value = "select cast(t.id as varchar)   as id,\n" +
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
            "where t.id = :ticketId")
    TicketProjection getTicketByTicketId(UUID ticketId);

    @Query(nativeQuery = true, value =
            "select not(t.status = 'PURCHASED'\n" +
            "        AND st.start_time+sd.date > now()+'10 minute')\n" +
            "from tickets t\n" +
            "join movie_sessions ms on ms.id = t.movie_session_id\n" +
            "join session_times st on ms.start_time_id = st.id\n" +
            "join session_dates sd on ms.date_id = sd.id\n" +
            "where t.id = :ticketId")
    boolean isExpired(UUID ticketId);

    @Query(nativeQuery = true,value = "select purchase_histories.intent_id from purchase_histories\n" +
            "join purchase_histories_tickets pht on purchase_histories.id = pht.purchase_history_id\n" +
            "join tickets t on pht.ticket_id = t.id\n" +
            "where t.id = :ticketId limit 1")
    String getPaymentIntentByTicketId(UUID ticketId);

    @Query(nativeQuery = true,value ="select coalesce((\n" +
            "                    select max(charge_fee_in_percentage)\n" +
            "                    from refund_charge_fees\n" +
            "                    where interval_in_minutes >\n" +
            "                          cast((select extract(epoch from (st.start_time + sd.date) - t.created_at) / 60) as int)\n" +
            "                ), (select min(charge_fee_in_percentage) from refund_charge_fees))\n" +
            "from purchase_histories ph\n" +
            "         join purchase_histories_tickets pht on pht.purchase_history_id = ph.id\n" +
            "         join tickets t on t.id = pht.ticket_id\n" +
            "         join movie_sessions ms on ms.id = t.movie_session_id\n" +
            "         join session_dates sd on ms.date_id = sd.id\n" +
            "         join session_times st on ms.start_time_id = st.id\n" +
            "where t.id = :ticketId\n")
    Double ticketRefundFeeInPer(UUID ticketId);

    @Query(nativeQuery = true,value = "select sum(t.price) from tickets t\n" +
            "join movie_sessions ms on t.movie_session_id = ms.id\n" +
            "join session_dates sd on ms.date_id = sd.id\n" +
            "where sd.date between :startDate and :endDate")
    Double getTotalIncome(Date startDate, Date endDate);

    @Query(nativeQuery = true,value = "select count(t.id) > 0 from tickets t where t.qr_code = :qrCode and t.status = 'PURCHASED' and t.movie_session_id = :sessionId")
    boolean checkTicket(String qrCode, UUID sessionId);
}
