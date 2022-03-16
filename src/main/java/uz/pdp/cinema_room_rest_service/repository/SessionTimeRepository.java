package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cinema_room_rest_service.model.SessionTimes;

import java.util.UUID;

public interface SessionTimeRepository extends JpaRepository<SessionTimes, UUID> {
}
