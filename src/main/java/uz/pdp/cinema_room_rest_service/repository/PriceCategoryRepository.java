package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.PriceCategory;

import java.util.Map;
import java.util.UUID;

public interface PriceCategoryRepository extends JpaRepository<PriceCategory, UUID> {

    @Query(nativeQuery = true, value = "select cast(id as varchar) as id,name, additional_fee_in_percentage as \"additionalFeeInPercentage\" from price_categories pc where pc.name like :search ")
    Page<Map<String, Object>> getAllCategories(Pageable pageable, String search);

    @Query(nativeQuery = true,value = "select cast(id as varchar) as id, name,created_at,updated_at from price_categories where id = :id")
    Map<String,Object> getCategoryById(UUID id);
}
