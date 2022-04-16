package uz.pdp.cinema_room_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinema_room_rest_service.dto.AnnouncementDto;
import uz.pdp.cinema_room_rest_service.dto.HallDateForBusyDateDto;
import uz.pdp.cinema_room_rest_service.dto.SessionDto;
import uz.pdp.cinema_room_rest_service.model.User;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.service.SessionService;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.UUID;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    SessionService sessionService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllSession(@RequestParam(required = false,defaultValue = "1") int page,
                                                     @RequestParam(required = false,defaultValue = "15") int size,
                                                     @RequestParam(required = false,defaultValue = "false") boolean expired
    ) {
        ApiResponse res = sessionService.getAllSession(page-1, size, expired);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getSessionById(@PathVariable UUID id){
        return ResponseEntity.ok(sessionService.getById(id));
    }

    @PreAuthorize("hasAuthority('add') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @PostMapping("/movie_announcement")
    public ResponseEntity<ApiResponse> saveSessions(@Valid  @RequestBody AnnouncementDto announcementDto){
        return sessionService.saveSession(announcementDto);
    }

    @PreAuthorize("hasAuthority('add') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @PostMapping
    public HttpEntity<ApiResponse> saveSession(@Valid @RequestBody SessionDto sessionDto, Authentication authentication){
        UUID userId = ((User) authentication.getPrincipal()).getId();
        return sessionService.saveSession(sessionDto , userId);
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse> checkHallsBusyTime(@RequestBody HallDateForBusyDateDto dto){
        return sessionService.checkBusyDateTimeHalls(dto.getStart(), dto.getEnd(), dto.getHallId());
    }

    @PreAuthorize("hasAuthority('delete') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public HttpEntity<ApiResponse> deleteSession(@PathVariable UUID id) {
        return sessionService.deleteSession(id);
    }

    @PreAuthorize("hasAuthority('delete') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @DeleteMapping
    public HttpEntity<ApiResponse> deleteByDateTime(@RequestParam Timestamp start, @RequestParam Timestamp end){
        return sessionService.deleteByDateTime(start, end);
    }








}
