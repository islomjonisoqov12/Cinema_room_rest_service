package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cinema_room_rest_service.model.Hall;
import uz.pdp.cinema_room_rest_service.model.PayType;

import java.util.UUID;

public interface PayTypeRepository extends JpaRepository<PayType, UUID> {
}
