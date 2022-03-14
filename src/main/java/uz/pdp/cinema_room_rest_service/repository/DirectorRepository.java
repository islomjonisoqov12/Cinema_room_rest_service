package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cinema_room_rest_service.model.Director;
import uz.pdp.cinema_room_rest_service.model.Hall;

import java.util.UUID;

public interface DirectorRepository extends JpaRepository<Director, UUID> {
}
