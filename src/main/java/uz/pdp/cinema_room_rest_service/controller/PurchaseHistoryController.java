package uz.pdp.cinema_room_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.service.HistoryService;

import java.util.UUID;

@Controller
@RequestMapping("/api/history")
public class PurchaseHistoryController {

    @Autowired
    HistoryService historyService;

    @GetMapping
    public HttpEntity<ApiResponse> getUserHistory(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "100") int size,
            @RequestParam(required = false, defaultValue = "%") String search,
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "asc") String direction
    ) {
        String userName = "wfilan0";
        return historyService.getUserHistoryByUserName(userName, page-1,size,search,sort, direction);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getHistoryById(@PathVariable UUID id){
        return historyService.getHistoryById(id);
    }



}
