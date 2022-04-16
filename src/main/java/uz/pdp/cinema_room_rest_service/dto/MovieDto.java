package uz.pdp.cinema_room_rest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Time;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import java.util.UUID;


@AllArgsConstructor @NoArgsConstructor @Data
public class MovieDto {

    UUID id;

    @NotEmpty(message = "the title must not be empty")
    @NotBlank(message = "the title must consist of characters")
    @NotNull(message = "you must input movie title")
    String title;

    @NotNull(message = "you must input movie duration")
    @Range(min = 1, message = "the movie duration must be greater than 0")
    Long time;


    double price;

    @NotNull(message = "input movie release date")
    Date releaseDate;

    double budget;

    @NotEmpty(message = "the status must not be empty")
    @NotBlank(message = "the status must consist of characters")
    @NotNull(message = "you must input movie status")
    String status;

    double distributorShareInPercentage;

    String description;

    MultipartFile video;

    List<MultipartFile> postImages;

    @NotNull(message = "select movie distributor")
    UUID distributor;

    @NotEmpty(message = "you don't select any countries")
    List<UUID> countries;

    @NotEmpty(message = "you don't select any genres")
    List<UUID> genres;

    @NotEmpty(message = "you don't select any actors")
    List<UUID> actors;

    @NotEmpty(message = "you don't select any directors")
    List<UUID> directors;


}
