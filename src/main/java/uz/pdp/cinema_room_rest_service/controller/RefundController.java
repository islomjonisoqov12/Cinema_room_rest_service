package uz.pdp.cinema_room_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.cinema_room_rest_service.model.User;
import uz.pdp.cinema_room_rest_service.service.TicketService;

import java.util.UUID;

@RestController
public class RefundController {
    @Autowired
    TicketService ticketService;

    @GetMapping("/api/refund/{ticketId}")
    public HttpEntity<?> refund(@PathVariable UUID ticketId, Authentication authentication){
        UUID userId = ((User) authentication.getPrincipal()).getId();
        return ticketService.refundTicket(userId,ticketId);
    }
}
