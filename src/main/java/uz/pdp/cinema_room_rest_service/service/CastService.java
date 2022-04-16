package uz.pdp.cinema_room_rest_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.cinema_room_rest_service.dto.CastDto;
import uz.pdp.cinema_room_rest_service.model.Actor;
import uz.pdp.cinema_room_rest_service.model.Attachment;
import uz.pdp.cinema_room_rest_service.model.Director;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.projection.ActorProjection;
import uz.pdp.cinema_room_rest_service.projection.CastProjection;
import uz.pdp.cinema_room_rest_service.repository.ActorRepository;
import uz.pdp.cinema_room_rest_service.repository.DirectorRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CastService {
    @Autowired
    ActorRepository actorRepository;

    @Autowired
    MovieService movieService;

    @Autowired
    DirectorRepository directorRepository;


    public HttpEntity<ApiResponse> getAllActors(int page, int size, String search, String sort) {
        ApiResponse response = new ApiResponse();
        try {
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort));
            Page<ActorProjection> allActorsPage = actorRepository.getAllActorsPage(pageable, search);
            response.setMessage("success");
            response.setData(allActorsPage);
            response.setSuccess(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return ResponseEntity.ok(response);
    }

    public HttpEntity<ApiResponse> saveActor(CastDto castDto) {
        ApiResponse response = new ApiResponse();
        try {
            Actor actor = new Actor();
            actor.setFullName(castDto.getFullName());
            actor.setDateOfBirth(castDto.getDateOfBirth().toLocalDate());
            actor.setId(castDto.getId());
            actor.setBio(castDto.getBio());
            if (castDto.getImg()!=null) {
                Attachment attachment = movieService.saveAttachment(castDto.getImg());
                actor.setImg(attachment);
            }
            actorRepository.save(actor);
            response.setMessage("success");
            response.setSuccess(true);
            response.setData(actor.getId());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    public HttpEntity<ApiResponse> deleteActorById(UUID id) {
        ApiResponse response = new ApiResponse();
        try {
            actorRepository.deleteById(id);
            response.setSuccess(true);
            response.setMessage("success");
        }catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return ResponseEntity.ok(response);
    }

    public HttpEntity<ApiResponse> getAllDirectors() {
        ApiResponse response = new ApiResponse();
        try {
            List<CastProjection> allDirectors = directorRepository.getAllDirectors();
            response.setData(allDirectors);
            response.setSuccess(true);
            response.setMessage("success");
        }catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return ResponseEntity.ok(response);
    }

    public HttpEntity<ApiResponse> saveDirector(CastDto castDto){
       ApiResponse response = new ApiResponse();
       try {
           Director director = new Director();
           director.setFullName(castDto.getFullName());
           director.setBio(castDto.getBio());
           director.setDateOfBirth(castDto.getDateOfBirth().toLocalDate());
           director.setId(castDto.getId());
           directorRepository.save(director);
           response.setMessage("success");
           response.setSuccess(false);
       }catch (Exception e) {
           response.setMessage(e.getMessage());
           response.setSuccess(false);
       }
       return ResponseEntity.ok(response);
    }


    public HttpEntity<?> deleteDirectorById(UUID id) {
        ApiResponse response  = new ApiResponse();
        try {
            directorRepository.deleteById(id);
            response.setSuccess(true);
            response.setMessage("Director successfully deleted");
        }catch (Exception e){
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}










