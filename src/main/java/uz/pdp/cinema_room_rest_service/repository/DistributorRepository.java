package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cinema_room_rest_service.model.Distributor;

import java.util.UUID;

public interface DistributorRepository extends JpaRepository<Distributor, UUID> {
}
