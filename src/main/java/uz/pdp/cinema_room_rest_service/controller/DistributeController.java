package uz.pdp.cinema_room_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinema_room_rest_service.dto.DistributorDto;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.service.DistributorService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/distributor")
public class DistributeController {


    @Autowired
    DistributorService distributorService;

    @GetMapping
    public HttpEntity<ApiResponse> getAllDistributor(@RequestParam(required = false, defaultValue = "1") int page , @RequestParam(required = false, defaultValue = "15") int size){
        ApiResponse response = distributorService.getAllDistributor(page-1,size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public HttpEntity<ApiResponse> getById(@PathVariable UUID id){
        return ResponseEntity.ok(distributorService.getById(id));
    }


    @PostMapping
    public HttpEntity<ApiResponse> saveDistributor(@Valid  DistributorDto distributorDto){
        ApiResponse response = distributorService.saveDistributor(distributorDto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('add') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @PutMapping
    public HttpEntity<ApiResponse> updateDistributor(DistributorDto distributorDto){
        ApiResponse response = distributorService.saveDistributor(distributorDto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('delete') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public HttpEntity<ApiResponse> deleteDistributor(@PathVariable UUID id){
        return distributorService.deleteById(id);
    }




}
