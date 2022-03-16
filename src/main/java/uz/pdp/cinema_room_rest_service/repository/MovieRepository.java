package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.Movie;
import uz.pdp.cinema_room_rest_service.projection.MovieProjection;

import java.util.List;
import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {

//    @Query(value = "select cast(m.id as varchar) as id, m.title as title, time, price, release_date as releaseDate, budget, description from movies m", nativeQuery = true)

    @Query(value = "select cast(id as varchar) as id, title, time as duration, release_date as releaseDate, budget, description from movies", nativeQuery = true)
    List<MovieProjection> getMovies(PageRequest pageRequest);
}
