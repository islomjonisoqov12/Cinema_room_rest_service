package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.Hall;
import uz.pdp.cinema_room_rest_service.model.Seat;
import uz.pdp.cinema_room_rest_service.projection.SeatProjection;

import java.util.List;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {

    @Query(nativeQuery = true, value = "select * from get_seats_for_purchase(:id);")
    List<SeatProjection> getAvailableSeats(UUID id);
}
