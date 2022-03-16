package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.Actor;
import uz.pdp.cinema_room_rest_service.model.Hall;
import uz.pdp.cinema_room_rest_service.projection.ActorProjection;

import java.util.List;
import java.util.UUID;

public interface ActorRepository extends JpaRepository<Actor, UUID> {

    @Query(value = "select\n" +
            "            cast(actors.id as varchar) as id,\n" +
            "            full_name as fullName,\n" +
            "            date_of_birth as dateOfBirth,\n" +
            "            bio  from actors join movies_actors ma on actors.id = ma.actor_id\n" +
            "                join movies m on ma.movie_id = m.id where m.id = :id", nativeQuery = true)
    List<ActorProjection> getActorByMovieId(UUID id);
}
