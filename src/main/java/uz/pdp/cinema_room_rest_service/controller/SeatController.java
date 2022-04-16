package uz.pdp.cinema_room_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.service.EmailSenderService;
import uz.pdp.cinema_room_rest_service.service.SeatService;

import java.util.UUID;

@RequestMapping("/api/seat")
@RestController
public class SeatController {
    @Autowired
    SeatService seatService;

    @Autowired
    EmailSenderService emailSenderService;

    @GetMapping("/available/{sessionId}")
    public HttpEntity<ApiResponse> getAvailableSeats(@PathVariable UUID sessionId){
        return seatService.getAvailableSeats(sessionId);
    }
}
