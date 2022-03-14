package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cinema_room_rest_service.model.Movie;

import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {
}
