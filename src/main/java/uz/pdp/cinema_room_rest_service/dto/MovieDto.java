package uz.pdp.cinema_room_rest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@AllArgsConstructor @NoArgsConstructor @Data
public class MovieDto {
    String title;

    Time time;

    double price;

    Date releaseDate;

    double budget;

    double distributorShareInPercentage;

    String description;

    MultipartFile video;

    MultipartFile postImg;

    UUID distributor;

    List<UUID> countries;

    List<UUID> genres;

    List<UUID> actors;

    List<UUID> directors;


}
