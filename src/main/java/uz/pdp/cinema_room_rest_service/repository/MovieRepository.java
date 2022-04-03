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

     @Query(nativeQuery = true,value = "")
    List<Map<String, Object>> getTopMovies(int size);
}
