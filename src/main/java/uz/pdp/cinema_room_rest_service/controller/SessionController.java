package uz.pdp.cinema_room_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.cinema_room_rest_service.projection.SessionProjection;
import uz.pdp.cinema_room_rest_service.service.SessionService;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    SessionService sessionService;

    @GetMapping
    public ResponseEntity getAllSession() {
        List<SessionProjection> allSession = sessionService.getAllSession();
        return ResponseEntity.ok(allSession);
    }



}
