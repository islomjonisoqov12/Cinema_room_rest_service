package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.Seat;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {

    @Query(nativeQuery = true, value = "select cast(sessionId as varchar) as \"sessionId \",\n" +
            "               cast(json_agg(json_build_object('rowNumber', rowNumber, 'seats', seats)) as text) as \"rows\"\n" +
            "        from (select hr.number         as rowNumber,\n" +
            "                     movie_sessions.id as sessionId,\n" +
            "                     json_agg(\n" +
            "                             json_build_object('id', cast(s.id as varchar),\n" +
            "                                               'seatNumber', s.number,\n" +
            "                                               'available', case\n" +
            "                                                                when t isnull or t.status = 'REFUNDED' then true\n" +
            "                                                                when t.id notnull then false end,\n" +
            "                                               'priceCategoryName', pc.name,\n" +
            "                                               'price', m.price +\n" +
            "                                                        m.price * pc.additional_fee_in_percentage / 100 +\n" +
            "                                                        m.price * h.vip_additional_fee_in_percentage / 100 +\n" +
            "                                                        m.price * st.session_additional_fee_in_percentage / 100\n" +
            "                                 ))    as seats\n" +
            "              from movie_sessions\n" +
            "                       join halls h on h.id = movie_sessions.hall_id\n" +
            "                       join hall_rows hr on h.id = hr.hall_id\n" +
            "                       join seats s on hr.id = s.row_id\n" +
            "                       left join tickets t on t.movie_session_id = movie_sessions.id and s.id = t.seat_id\n" +
            "                       join price_categories pc on s.price_category_id = pc.id\n" +
            "                       join movie_announcements ma on movie_sessions.movie_announcement_id = ma.id\n" +
            "                       join movies m on ma.movie_id = m.id\n" +
            "                       join session_times st on movie_sessions.start_time_id = st.id\n" +
            "              where movie_sessions.id = :sessionId\n" +
            "              group by hr.number, h.id, movie_sessions.id\n" +
            "              order by hr.number) as hallRows\n" +
            "        group by sessionId")
    List<Map<String,Object>> getAvailableSeats(UUID sessionId);

    @Query(nativeQuery = true,value = "select count(*)>0 as test from movie_sessions\n" +
            "join halls h on movie_sessions.hall_id = h.id\n" +
            "join hall_rows hr on h.id = hr.hall_id\n" +
            "join seats s on hr.id = s.row_id\n" +
            "where movie_sessions.id = :sessionId\n" +
            "and s.id = :seatId")
    boolean existsSeatBySessionIdAndSeatId(UUID sessionId, UUID seatId);

}
