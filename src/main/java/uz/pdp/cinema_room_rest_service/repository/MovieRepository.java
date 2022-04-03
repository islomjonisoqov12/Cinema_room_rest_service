package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.Movie;
import uz.pdp.cinema_room_rest_service.projection.MovieByIdProjection;
import uz.pdp.cinema_room_rest_service.projection.MovieProjection;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {

    @Query(value = "select cast(id as varchar) as id, title, time as duration, release_date as releaseDate, budget, description from movies", nativeQuery = true)
    List<MovieProjection> getMovies(PageRequest pageRequest);

    @Query(value = "select cast(movies.id as varchar) as id," +
            " movies.title as title," +
            " movies.time as  duration," +
            " movies.release_date as releaseDate," +
            " movies.budget as budget, " +
            " movies.description as description," +
            " cast(a.id as varchar) as img," +
            " cast(a1.id as varchar) as trailer " +
            " from  movies left join attachments a on movies.poster_img = a.id" +
            " left join attachments a1 on a1.id = movies.trailer_video" +
            " where movies.id = :id", nativeQuery = true)
    List<MovieByIdProjection> getMovieByMovieId(UUID id);

    @Query(value = "select cast(max(sp.price_seats) as double precision) from get_available_seat_prices_by_session_id(:id) as sp",nativeQuery = true)
    Double getMaxPrice(UUID id);

     @Query(value = "select cast(min(sp.price_seats) as double precision) from get_available_seat_prices_by_session_id(:id) as sp",nativeQuery = true)
    Double getMinPrice(UUID id);

     @Query(nativeQuery = true,value = "select distinct\n" +
             "                cast(m.id as varchar)                                                                                           as id,\n" +
             "       m.title                                                                                                                  as title,\n" +
             "       cast(m.poster_img as varchar)                                                                                            as image_id,\n" +
             "       count(t.id)                                                                                                              as purchased_ticket_count,\n" +
             "       count(distinct ms.id)                                                                                                    as sessions_count,\n" +
             "       (select sum(ph3.total_amount) from purchase_histories ph3 where ph3.id = ph.id  and not ph3.refund)                      as total_amount,\n" +
             "       (select (select sum(ph3.total_amount) from purchase_histories ph3 where ph3.id = ph.id and not ph3.refund)\n" +
             "                   - (select sum(ph2.total_amount) from purchase_histories ph2 where ph2.id = ph.id and ph2.refund)\n" +
             "                   - m.price * (select count(*) from tickets t where t.movie_session_id = ms.id and t.status = 'PURCHASED'))    as net,\n" +
             "       m.price * (select count(*) from tickets t where t.movie_session_id = ms.id and t.status = 'PURCHASED')                   as for_distributor\n" +
             "from movies m\n" +
             "        join movie_announcements ma on m.id = ma.movie_id\n" +
             "        join movie_sessions ms on ma.id = ms.movie_announcement_id\n" +
             "        join tickets t on ms.id = t.movie_session_id\n" +
             "        join purchase_histories_tickets pht on t.id = pht.ticket_id\n" +
             "        join purchase_histories ph on pht.purchase_history_id = ph.id\n" +
             "where t.status = 'PURCHASED'\n" +
             "group by m.id , ph.id, ms.id\n" +
             "order by count(t.id) DESC\n" +
             "limit :size")
    List<Map<String, Object>> getTopMovies(int size);
}
