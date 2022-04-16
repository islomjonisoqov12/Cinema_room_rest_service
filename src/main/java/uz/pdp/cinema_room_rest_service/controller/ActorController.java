package uz.pdp.cinema_room_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.cinema_room_rest_service.dto.CastDto;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.service.CastService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ActorController {

    @Autowired
    CastService castService;

    @PermitAll
    @GetMapping("/actor")
    public HttpEntity<ApiResponse> getAllActors(
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "50") int size,
            @RequestParam(required = false,defaultValue = "%") String search,
            @RequestParam(required = false,defaultValue = "id") String sort
    ){
        return castService.getAllActors(page,size,search,sort);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_SUPER_ADMIN"})
    @PreAuthorize("hasAuthority('add') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @PostMapping("/actor")
    public HttpEntity<ApiResponse> saveActors(@Valid @RequestPart CastDto castDto, @RequestPart MultipartFile img){
        castDto.setImg(img);
        return castService.saveActor(castDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PutMapping("/actor")
    public HttpEntity<ApiResponse> update(@Valid CastDto castDto) {
        return castService.saveActor(castDto);
    }

    @PreAuthorize("hasAuthority('delete') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @DeleteMapping("/actor/{id}")
    public HttpEntity<ApiResponse> deleteActor(@PathVariable UUID id){
        return castService.deleteActorById(id);
    }


    @GetMapping("/director")
    public HttpEntity<ApiResponse> getAllDirector(){
        return castService.getAllDirectors();
    }

    @PreAuthorize("hasAuthority('add') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @PostMapping("/director")
    public HttpEntity<?> saveDirector(@Valid  @RequestBody CastDto castDto){
        return castService.saveDirector(castDto);
    }

    @PreAuthorize("hasAuthority('delete') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @DeleteMapping("/director/{id}")
    public HttpEntity<?> deleteDirector(@PathVariable UUID id){
        return castService.deleteDirectorById(id);
    }

    @PreAuthorize("hasAuthority('add') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @PutMapping("/director")
    public HttpEntity<ApiResponse> editDirector(@Valid @RequestBody CastDto castDto){
        return castService.saveDirector(castDto);
    }




}
