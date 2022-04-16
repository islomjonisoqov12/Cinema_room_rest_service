package uz.pdp.cinema_room_rest_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.projection.HistoryByIdProjection;
import uz.pdp.cinema_room_rest_service.projection.HistoryProjection;
import uz.pdp.cinema_room_rest_service.repository.PurchaseHistoryRepository;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class HistoryService {

    @Autowired
    PurchaseHistoryRepository historyRepository;

    public HttpEntity<ApiResponse> getUserHistoryByUserName(String userName, int page, int size, String search, String sort, String direction) {
        ApiResponse response = new ApiResponse();
        try {
            Page<HistoryProjection> histories = historyRepository.findUserHistoryByUserName(userName, search ,PageRequest.of(page, size, Sort.by(direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort)));
            response.setMessage("success");
            response.setSuccess(true);
            response.setData(histories);
        }catch (Exception e){
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(response);

    }

    public HttpEntity<?> getHistoryById(UUID id) {
        ApiResponse response = new ApiResponse();
        try {
            HistoryByIdProjection byHistoryId = historyRepository.findByHistoryId(id);
            response.setMessage("success");
            response.setData(byHistoryId);
            response.setSuccess(true);
        }catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
