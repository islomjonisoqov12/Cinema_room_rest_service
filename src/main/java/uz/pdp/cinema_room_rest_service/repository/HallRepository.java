package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.Hall;
import uz.pdp.cinema_room_rest_service.projection.HallBusyDate;
import uz.pdp.cinema_room_rest_service.projection.HallBusyTime;
import uz.pdp.cinema_room_rest_service.projection.HallProjection;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface HallRepository extends JpaRepository<Hall, UUID> {

    Hall findByName(String name);


    @Query(value = "select cast(h.id as varchar) as id,\n" +
            "                   h.name as name,\n" +
            "                   (select case\n" +
            "                       when vip_additional_fee_in_percentage=0.0 then false\n" +
            "                       when vip_additional_fee_in_percentage>0.0 then true\n" +
            "                       end vip\n" +
            "                       ),\n" +
            "                cast(ms.id as varchar) as sessionId\n" +
            "            from halls h\n" +
            "                     join movie_sessions rh on h.id = rh.hall_id\n" +
            "                     join movie_announcements ms on rh.movie_announcement_id = ms.id\n" +
            "            where ms.id = :sessionId and rh.date_id = :id", nativeQuery = true)
    List<HallProjection> getHallsBySessionId(UUID id, UUID sessionId);


    @Query(nativeQuery = true,value = "select distinct cast(h.id as varchar) as id,\n" +
            "       sd.date               as date\n" +
            "from halls h\n" +
            "         join movie_sessions ms on h.id = ms.hall_id\n" +
            "         join session_dates sd on ms.date_id = sd.id\n" +
            "where sd.date >= :start \n" +
            "  and sd.date <= :end \n" +
            "  and h.id = :hallId")
    List<HallBusyDate> getBusyDateTime(Date start, Date end, UUID hallId);



    @Query(value = "select distinct st.start_time as startTime,\n" +
            "       ms.end_time   as endTime\n" +
            "from halls h\n" +
            "         join movie_sessions ms on h.id = ms.hall_id\n" +
            "         join session_times st on ms.start_time_id = st.id\n" +
            "         join session_dates sd on ms.date_id = sd.id\n" +
            "where h.id = :hallId\n" +
            "  and sd.date = :date ", nativeQuery = true)
    List<HallBusyTime> getBusyTimeByDateAndHallId(Date date, UUID hallId);

//
//    @Query(value = "select cast(st.id as varchar) as id,\n" +
//            "       st.start_time as startTime,\n" +
//            "       rh.end_time as endTime\n" +
//            "from movie_sessions ms\n" +
//            "         join movie_sessions rh on ms.id = rh.announcement_id\n" +
//            "         join session_times st on rh.start_time_id = st.id\n" +
//            "where rh.hall_id = :hallId\n" +
//            "  and ms.id = :sessionId",nativeQuery = true)
//    List<SessionTimesProjection> getGetTimeBySessionAndHallId(UUID id, UUID sessionId);

}
