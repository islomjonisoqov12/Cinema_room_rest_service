package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.cinema_room_rest_service.model.Genre;
import uz.pdp.cinema_room_rest_service.projection.GenreProjection;

import java.util.List;
import java.util.UUID;
@RepositoryRestResource(collectionResourceRel = "genres", path = "genres")
public interface GenreRepository extends JpaRepository<Genre, UUID> {
    @Query(value = "select cast(g.id as varchar) as id, g.name as name from  genres g " +
            "join movies_genres mg on g.id = mg.genre_id " +
            "join movies m on m.id = mg.movie_id where m.id = :id",nativeQuery = true)
    List<GenreProjection> getFindByMovieId(UUID id);

}
