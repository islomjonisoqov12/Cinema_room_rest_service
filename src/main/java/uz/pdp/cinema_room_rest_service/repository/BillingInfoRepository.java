package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cinema_room_rest_service.model.BillingInfo;
import uz.pdp.cinema_room_rest_service.model.Hall;

import java.util.UUID;

public interface BillingInfoRepository extends JpaRepository<BillingInfo, UUID> {
}
