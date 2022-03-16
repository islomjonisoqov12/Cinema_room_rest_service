package uz.pdp.cinema_room_rest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinema_room_rest_service.dto.MovieDto;
import uz.pdp.cinema_room_rest_service.model.Movie;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.service.MovieService;

import java.util.UUID;

@RequestMapping("/api")
@RestController
public class MovieController {

    @Autowired
    MovieService movieService;

    @GetMapping("/movies/{movieId}")
    public Movie getSessions(@PathVariable UUID movieId) {
        return movieService.getMovieById(movieId);
    }

    @PostMapping("/movies")
    public HttpEntity<ApiResponse> saveMovie(@RequestBody MovieDto movieDto) {
        ApiResponse response = new ApiResponse();
        movieService.saveMovie(movieDto, response);
        return ResponseEntity.ok(response);
    }



}
