package uz.pdp.cinema_room_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinema_room_rest_service.model.User;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.service.TicketService;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {
    @Autowired
    TicketService ticketService;

    @PostMapping("/purchase")
    public HttpEntity<?> purchaseGenerateCode(Authentication authentication) {
        UUID userId = ((User) authentication.getPrincipal()).getId();
        return ticketService.createStripeSession(userId);
//        return ticketService.purchaseTicket(userId);
    }
    @GetMapping("/show_bucket")
    public HttpEntity<ApiResponse> getUserTickets(Authentication authentication){
        UUID userId = ((User) authentication.getPrincipal()).getId();
        return ticketService.getUserTickets(userId);
    }

    @PostMapping("/check")
    public HttpEntity<ApiResponse> checkTicket(@RequestBody Map<String,Object> check) {
        return ticketService.checkTicket(check.get("qrCode").toString(), UUID.fromString(check.get("sessionId").toString()));
    }

    @DeleteMapping("/{id}")
    public HttpEntity<ApiResponse> deleteTicket(@PathVariable UUID id) {
        return ticketService.deleteTicketById(id);
    }

    @PostMapping("/add_cart")
    public HttpEntity<ApiResponse> addToCard(@RequestBody Map<String, Object> ticketDto, Authentication authentication) {
        UUID userId = ((User) authentication.getPrincipal()).getId();
        return ticketService.addToCard(userId, UUID.fromString(ticketDto.get("seatId").toString()), UUID.fromString(ticketDto.get("sessionId").toString()));
    }


}
