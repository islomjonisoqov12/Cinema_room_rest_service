package uz.pdp.cinema_room_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinema_room_rest_service.model.PriceCategory;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.repository.PriceCategoryRepository;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
public class PriceCategoryController {

    @Autowired
    PriceCategoryRepository categoryRepository;


    @GetMapping
    public HttpEntity<ApiResponse> getAllPriceCategories(
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "30") int size,
            @RequestParam(required = false,defaultValue = "%%%") String search,
            @RequestParam(required = false,defaultValue = "id") String sort
    ){
        return ResponseEntity.ok(new ApiResponse("success",true, categoryRepository.getAllCategories(
                PageRequest.of(page-1,size, Sort.by(sort)),search
        )));
    }

    @GetMapping("/{id}")
    public HttpEntity<ApiResponse> getCategoryById(@PathVariable UUID id){
        return ResponseEntity.ok(new ApiResponse("success", true,categoryRepository.getCategoryById(id)));
    }

    @PreAuthorize("hasAuthority('add') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @PostMapping
    public HttpEntity<ApiResponse> savePriceCategory(@RequestBody Map<String,Object> category){
        return ResponseEntity.ok(new ApiResponse("success",true,
                categoryRepository.save(new PriceCategory(category.get("name").toString(),(Double) category.get("additionalFeeInPercentage"))).getId()
        ));
    }

    @PreAuthorize("hasAuthority('add') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @PutMapping
    public HttpEntity<ApiResponse> editPriceCategory(@RequestBody Map<String,Object> category){
        return ResponseEntity.ok(new ApiResponse("success",true,
                categoryRepository.save(new PriceCategory(UUID.fromString(category.get("id").toString()),category.get("name").toString(),(Double) category.get("additionalFeeInPercentage"))).getId()
        ));
    }
}






