package uz.pdp.cinema_room_rest_service.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinema_room_rest_service.model.Distributor;
import uz.pdp.cinema_room_rest_service.projection.DistributeProjection;

import java.util.List;
import java.util.UUID;

public interface DistributorRepository extends JpaRepository<Distributor, UUID> {

    @Query(value = "select cast(id as varchar) as id, name, description , cast(img_id as varchar) as imgId from distributors", nativeQuery = true)
    List<DistributeProjection> getAllDistributor(PageRequest pageRequest);

    @Query(value = "select cast(id as varchar) as id, name, description , cast(img_id as varchar) as imgId from distributors where id = :id",nativeQuery = true)
    DistributeProjection getDistributorById(UUID id);
}
