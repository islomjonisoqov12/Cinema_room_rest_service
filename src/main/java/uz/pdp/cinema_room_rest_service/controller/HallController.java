package uz.pdp.cinema_room_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinema_room_rest_service.dto.RowDto;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.service.HallService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hall")
public class HallController {

    @Autowired
    HallService hallService;

    @GetMapping("/{hallId}")
    public HttpEntity<ApiResponse>   getBusyTime(@RequestParam String startDate, @RequestParam(required = false) String endDate, @PathVariable UUID hallId){
        return hallService.getBusyTime(startDate,endDate,hallId);
    }

    @PreAuthorize("hasAuthority('add') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @PostMapping("/save_rows/{hallId}")
    public HttpEntity<?> saveRows(@Valid @RequestBody List<RowDto> rowDtoList, @PathVariable UUID hallId){
        return hallService.saveRows(rowDtoList, hallId);
    }

}
















