package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cinema_room_rest_service.model.Cart;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}
