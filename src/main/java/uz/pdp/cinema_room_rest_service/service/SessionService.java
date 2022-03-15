package uz.pdp.cinema_room_rest_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cinema_room_rest_service.projection.SessionProjection;
import uz.pdp.cinema_room_rest_service.repository.MovieSessionRepository;

import java.util.List;

@Service
public class SessionService {

    @Autowired
    MovieSessionRepository sessionRepository;

    public List<SessionProjection> getAllSession() {
        return sessionRepository.getAllSession();
    }
}
