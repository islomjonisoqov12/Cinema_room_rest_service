package uz.pdp.cinema_room_rest_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import uz.pdp.cinema_room_rest_service.dto.RowDto;
import uz.pdp.cinema_room_rest_service.dto.SeatDto;
import uz.pdp.cinema_room_rest_service.model.Hall;
import uz.pdp.cinema_room_rest_service.model.PriceCategory;
import uz.pdp.cinema_room_rest_service.model.Row;
import uz.pdp.cinema_room_rest_service.model.Seat;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.repository.HallRepository;
import uz.pdp.cinema_room_rest_service.repository.PriceCategoryRepository;
import uz.pdp.cinema_room_rest_service.repository.RowRepository;
import uz.pdp.cinema_room_rest_service.repository.SeatRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HallService {

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    HallRepository hallRepository;

    @Autowired
    RowRepository rowRepository;

    @Autowired
    SessionService sessionService;

    @Autowired
    PriceCategoryRepository priceCategoryRepository;


    public HttpEntity<ApiResponse> getBusyTime(String startDate, String endDate, UUID hallId) {
        return null;
    }

    public HttpEntity<?> saveRows(List<RowDto> rowDtos, UUID hallId) {
        ApiResponse response = new ApiResponse();
        try {
            Optional<Hall> hallOptional = hallRepository.findById(hallId);
            if (hallOptional.isPresent()) {
                Hall hall = hallOptional.get();
                for (RowDto row : rowDtos) {
                    Row row1 = new Row();
                    row1.setHall(hall);
                    row1.setNumber(row.getRowNumber());
                    rowRepository.save(row1);
                    int seatNumber = 0;
                    for (SeatDto seatDto : row.getSeatDtoList()) {
                        if(seatNumber == 0) seatNumber = seatDto.getBeginNumber();
                        Seat seat = new Seat();
                        Optional<PriceCategory> priceCategoryOptional = priceCategoryRepository.findById(seatDto.getPriceCategoryId());
                        if (priceCategoryOptional.isPresent()) {
                            PriceCategory priceCategory = priceCategoryOptional.get();
                            seat.setPriceCategory(priceCategory);
                        }else throw new ResourceNotFoundException("price category does not exists");
                        seat.setNumber(seatNumber);
                        seat.setRow(row1);
                        seatRepository.save(seat);
                        row1.addSeats(seat);
                    }
                    rowRepository.save(row1);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(response);

    }
}
