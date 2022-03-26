package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.Hall;
import uz.pdp.cinema_room_rest_service.model.PurchaseWaitingTime;

import java.util.Optional;
import java.util.UUID;

public interface PurchaseWaitingTimeRepository extends JpaRepository<PurchaseWaitingTime, UUID> {

    @Query(nativeQuery = true,value = "select * from purchase_waiting_time limit 1")
    Optional<PurchaseWaitingTime> findFirstTime();
}
