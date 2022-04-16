package uz.pdp.cinema_room_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.cinema_room_rest_service.dto.MovieDto;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.service.MovieService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/movie")
@RestController
public class MovieController {

    @Autowired
    MovieService movieService;

    @GetMapping
    public ResponseEntity<ApiResponse> getMovies(@RequestParam(required = false, defaultValue = "1") int page,@RequestParam(required = false, defaultValue = "15") int size){
        ApiResponse response = new ApiResponse();
        ApiResponse movies = movieService.getMovies(response, page -1, size);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<ApiResponse> getMovieById(@PathVariable UUID movieId) {
        ApiResponse response = movieService.getMovieById(movieId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('add') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @PostMapping(consumes = {"multipart/form-data"})
    public HttpEntity<ApiResponse> saveMovie(@Valid  @RequestPart MovieDto data, @RequestPart List<MultipartFile> postImages11, @RequestPart MultipartFile video11) {
        ApiResponse response = new ApiResponse();
        data.setPostImages(postImages11);
        data.setVideo(video11);
        movieService.saveMovie(data, response);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('add') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @PutMapping
    public HttpEntity<ApiResponse> updateMovie(MovieDto movieDto){
        ApiResponse response = new ApiResponse();
        movieService.saveMovie(movieDto,response);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('delete') or hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public HttpEntity<ApiResponse> deleteMovieById(@PathVariable UUID id){
        ApiResponse response = movieService.deleteMovie(id);
        return ResponseEntity.ok(response);
    }









}
