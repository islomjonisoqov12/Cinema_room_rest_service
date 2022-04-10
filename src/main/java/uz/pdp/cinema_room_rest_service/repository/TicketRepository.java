package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.Ticket;
import uz.pdp.cinema_room_rest_service.projection.TicketProjection;

import java.sql.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Optional<Ticket> findByQrCodeAndMovieSessionId(String qrCode, UUID movieSession_id);

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

    @Query(nativeQuery = true, value = "select purchase_histories.intent_id from purchase_histories\n" +
            "join purchase_histories_tickets pht on purchase_histories.id = pht.purchase_history_id\n" +
            "join tickets t on pht.ticket_id = t.id\n" +
            "where t.id = :ticketId limit 1")
    String getPaymentIntentByTicketId(UUID ticketId);

    @Query(nativeQuery = true, value = "select coalesce((\n" +
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

    /*
    total income
    total outcome
     [
     each announcement - pay for distributor amount
    ]
     */
    @Query(nativeQuery = true,
            value = "select sum(prs.income)             as \"totalIncome\",\n" +
            "       sum(prs.outcome)            as \"totalOutcome\",\n" +
            "       sum(prs.net)                as \"totalNet\",\n" +
            "       cast(json_agg(prs) as text) as \"eachION\"\n" +
            "from (select \"movieAnnouncementId\",\n" +
            "             \"movieTitle\",\n" +
            "             distributor,\n" +
            "             sum(outcome) as outcome,\n" +
            "             sum(income)  as income,\n" +
            "             sum(net)     as net\n" +
            "      from (select cast(ma.id as varchar)                                                                                as \"movieAnnouncementId\",\n" +
            "                   m.title                                                                                               as \"movieTitle\",\n" +
            "                   d.name                                                                                                as \"distributor\",\n" +
            "                   coalesce((m.price * count(distinct t.id)) + (case when ph.refund then ph.total_amount else 0 end),\n" +
            "                            0)                                                                                           as outcome,\n" +
            "                   coalesce(sum(t.price), 0)                                                                             as income,\n" +
            "                   coalesce(sum(t.price) -\n" +
            "                            (m.price * count(distinct t.id) + case when ph.refund then ph.total_amount else 0 end),\n" +
            "                            0)                                                                                           as net\n" +
            "            from movie_announcements ma\n" +
            "                     full join movie_sessions ms on ma.id = ms.movie_announcement_id\n" +
            "                     full join movies m on ma.movie_id = m.id\n" +
            "                     full join tickets t on ms.id = t.movie_session_id\n" +
            "                     full join distributors d on m.distributed_by = d.id\n" +
            "                     full join purchase_histories_tickets pht on t.id = pht.ticket_id\n" +
            "                     full join purchase_histories ph on pht.purchase_history_id = ph.id\n" +
            "            where (t.status = 'PURCHASED' or t.id is null or ph.refund)\n" +
            "              and (case when :checkDate then ph.created_at between :startDate and :endDate else true end)\n" +
            "            group by ma.id, m.title, m.price, d.name, ph.refund, ph.total_amount\n" +
            "            order by net desc) as ins\n" +
            "      group by \"movieAnnouncementId\", \"movieTitle\", distributor order by income desc) as prs")
    Map<String, Object> getTotalIncomeOutcomeNet(Date startDate, Date endDate, boolean checkDate);

    @Query(nativeQuery = true, value = "select count(t.id) > 0 from tickets t where t.qr_code = :qrCode and t.status = 'PURCHASED' and t.movie_session_id = :sessionId and not checked")
    boolean checkTicket(String qrCode, UUID sessionId);

    @Query(nativeQuery = true,
            value = "select :page as page,\n" +
                    "       :size as size,\n" +
                    "       cast(json_agg(json_build_object('date', data.date, 'ticketCount', data.count)) as text) as \"tickets\",\n" +
                    "       coalesce((select count(*) over ()\n" +
                    "        from tickets\n" +
                    "        where (created_at between :startDate and :endDate) or cast(created_at as date) = :startDate \n" +
                    "        group by cast(created_at as date)\n" +
                    "        limit 1),0)as \"totalElementCount\"\n" +
                    "from (select cast(created_at as date) as date,\n" +
                    "             count(id)                as count\n" +
                    "      from tickets\n" +
                    "      where (created_at between :startDate and :endDate) or cast(created_at as date) = :startDate \n" +
                    "      group by date\n" +
                    "      limit :size offset :size * :page) as data\n")
    Map<String,Object> getTicketSoldAmountPerDay(Date startDate, Date endDate, int page, int size);

    @Query(nativeQuery = true,
            value = "select :page                                                                                      as page,\n" +
                    "       :size                                                                                      as size,\n" +
                    "       cast(json_agg(json_build_object('date', data.date, 'ticketCount', data.count)) as text) as \"tickets\",\n" +
                    "       coalesce((select count(*) over ()\n" +
                    "        from tickets" +
                    "        group by cast(created_at as date) limit 1),0)                                               as \"totalElementCount\"\n" +
                    "from (select cast(created_at as date) as date,\n" +
                    "             count(id)                as count\n" +
                    "      from tickets\n" +
                    "      group by date\n" +
                    "      limit :size offset :size * :page) as data")
    Map<String,Object> getTicketSoldAmountPerDay(int page, int size);
}
