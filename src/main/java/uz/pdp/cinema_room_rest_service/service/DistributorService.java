package uz.pdp.cinema_room_rest_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.cinema_room_rest_service.dto.DistributorDto;
import uz.pdp.cinema_room_rest_service.model.Attachment;
import uz.pdp.cinema_room_rest_service.model.Distributor;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.projection.DistributeProjection;
import uz.pdp.cinema_room_rest_service.repository.DistributorRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DistributorService {

    @Autowired
    DistributorRepository distributorRepository;

    @Autowired
    MovieService movieService;

    public ApiResponse getAllDistributor(int page, int size) {
        ApiResponse response = new ApiResponse();
        try {
            List<DistributeProjection> allDistributor = distributorRepository.getAllDistributor(PageRequest.of(page,size));
            response.setSuccess(true);
            response.setMessage("success");
            response.setData(allDistributor);
        }catch (Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public ApiResponse saveDistributor(DistributorDto distributorDto) {
        ApiResponse response = new ApiResponse();
        try {
            Distributor distributor = new Distributor();
            distributor.setId(distributorDto.getId());
            distributor.setName(distributorDto.getName());
            distributor.setDescription(distributorDto.getDescription());
            Attachment attachment = movieService.saveAttachment(distributorDto.getImg());
            distributor.setImg(attachment);
            distributorRepository.save(distributor);
        }catch (Exception e){
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return response;
    }

    public ApiResponse getById(UUID id) {
        ApiResponse response = new ApiResponse();
        try {
            DistributeProjection distributorById = distributorRepository.getDistributorById(id);
            response.setSuccess(true);
            response.setData(distributorById);
        }catch (Exception e){
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return response;
    }

    public HttpEntity<ApiResponse> deleteById(UUID id) {
        ApiResponse response = new ApiResponse();
        try {
            distributorRepository.deleteById(id);
            response.setMessage("successfully deleted");
            response.setSuccess(true);
            response.setData(id);
        }catch (Exception e){
            response.setData(id);
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
