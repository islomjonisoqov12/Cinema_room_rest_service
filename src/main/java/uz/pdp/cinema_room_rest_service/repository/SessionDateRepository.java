package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cinema_room_rest_service.model.SessionDates;

import java.util.UUID;

public interface SessionDateRepository extends JpaRepository<SessionDates, UUID> {
}
