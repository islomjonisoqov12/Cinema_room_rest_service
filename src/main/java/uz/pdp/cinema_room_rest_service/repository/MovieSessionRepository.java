package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cinema_room_rest_service.model.MovieSession;

import java.util.UUID;

public interface MovieSessionRepository extends JpaRepository<MovieSession, UUID> {

}
