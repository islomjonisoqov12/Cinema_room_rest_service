package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "select " +
            "cast(a.id as varchar) as id," +
            "a.full_name as fullName," +
            "a.date_of_birth as dateOfBirth," +
            "a.bio as bio  from actors a where a.full_name like concat('%',:search,'%')",nativeQuery = true)
    Page<ActorProjection> getAllActorsPage(Pageable pageable, String search);
}
