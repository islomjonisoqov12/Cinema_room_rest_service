package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.cinema_room_rest_service.model.Director;
import uz.pdp.cinema_room_rest_service.model.Hall;
import uz.pdp.cinema_room_rest_service.projection.ActorProjection;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "director", path = "director")
public interface DirectorRepository extends JpaRepository<Director, UUID> {

    @Query(value = "select cast(d.id as varchar) as id,\n" +
            "             d.full_name as fullName,\n" +
            "             d.date_of_birth as dateOfBirth,\n" +
            "             d.bio as bio from directors d\n" +
            "                 join movies_directors md on d.id = md.director_id\n" +
            "                 join movies m on m.id = md.movie_id where m.id = :id",
            nativeQuery = true)
    List<ActorProjection> getFindByMovieId(UUID id);
}
