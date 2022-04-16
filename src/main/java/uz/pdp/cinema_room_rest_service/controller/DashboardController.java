package uz.pdp.cinema_room_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinema_room_rest_service.model.CashBox;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.repository.CashBoxRepository;
import uz.pdp.cinema_room_rest_service.service.DashboardService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.Map;
import java.util.UUID;

@RestController
@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN') or hasAuthority('transaction')")
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    @Autowired
    CashBoxRepository cashBoxRepository;

    /**
     * income,  outcome by date
     */
    @GetMapping("/costs")
    public HttpEntity<?> getCosts(
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate
    ) {
        return dashboardService.getCosts(startDate, endDate);
    }

    @GetMapping("/sold/ticket/perDay")
    public HttpEntity<?> getAmountTicketSoldPerDay(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "50") int size,
            @RequestBody(required = false)Map<String, Object> date
            ) {
        return dashboardService.getAmountTicketSoldPerDay(page-1,size, date);
    }


    @GetMapping("/top/sessions")
    public HttpEntity<?> getTopSession(
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "false") boolean expired
    ) {
        return dashboardService.getTopSession(size,expired);
    }

    @GetMapping("/top/movies")
    public HttpEntity<?> getTopFilms(
            @RequestParam(required = false, defaultValue = "5") int size
    ) {
        return dashboardService.getTopMovies(size);
    }

    @GetMapping("/cashBox")
    public HttpEntity<?> getCashBox(){
        return ResponseEntity.ok(new ApiResponse("success",true,cashBoxRepository.findAll()));
    }

    @GetMapping("/cashBox/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id){
        return ResponseEntity.ok(new ApiResponse("success",true,cashBoxRepository.findById(id)));
    }

    @PreAuthorize("hasAuthority('transaction')")
    @PostMapping("/cashBox/transaction")
    public HttpEntity<?> transaction(@RequestBody Map<String, Object> cashBox){
        return dashboardService.transaction(cashBox);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/cashBox")
    public HttpEntity<?> saveCashBox(@RequestBody @NotEmpty @NotNull @NotBlank String name){
        return ResponseEntity.ok(
                new ApiResponse("success",true,
                        cashBoxRepository.save(
                                new CashBox(null,name,0.0)).getId()));
    }



}
