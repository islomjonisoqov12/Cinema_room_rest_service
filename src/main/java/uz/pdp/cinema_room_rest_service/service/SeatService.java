package uz.pdp.cinema_room_rest_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cinema_room_rest_service.model.PriceCategory;
import uz.pdp.cinema_room_rest_service.model.Row;
import uz.pdp.cinema_room_rest_service.model.Seat;
import uz.pdp.cinema_room_rest_service.repository.HallRepository;
import uz.pdp.cinema_room_rest_service.repository.PriceCategoryRepository;
import uz.pdp.cinema_room_rest_service.repository.RowRepository;
import uz.pdp.cinema_room_rest_service.repository.SeatRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class SeatService {

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    HallRepository hallRepository;

    @Autowired
    RowRepository rowRepository;

    @Autowired
    PriceCategoryRepository priceCategoryRepository;

    public List<Seat> saveSeat(UUID priceCategoryId, UUID rowId, int begin, int end ){
        Optional<PriceCategory> priceCategory = priceCategoryRepository.findById(priceCategoryId);
        Optional<Row> row = rowRepository.findById(rowId);
        if(row.isPresent() && priceCategory.isPresent()){
            List<Seat> seats = new ArrayList<>();
            for (int i=begin; i<= end; i++){
                Seat seat = new Seat();
                seat.setNumber(i);
                seat.setRow(row.get());
                seat.setPriceCategory(priceCategory.get());
                seat = seatRepository.save(seat);
                seats.add(seat);
            }
            return seats;
        }
        return null;
    }

}
