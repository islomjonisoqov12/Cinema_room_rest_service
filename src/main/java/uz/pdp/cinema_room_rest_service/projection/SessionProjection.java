package uz.pdp.cinema_room_rest_service.projection;

import org.springframework.beans.factory.annotation.Value;
import uz.pdp.cinema_room_rest_service.enums.MovieStatus;

import java.time.LocalDate;

public interface SessionProjection{

    String getId();

    String getMovieId();

    String getMovieTitle();

    String getImgId();

    MovieStatus getMovieStatus();

    @Value("#{@movieAnnouncementRepository.getStartDate(target.id)}")
    LocalDate getStartDate();

    @Value("#{@movieAnnouncementRepository.getEndDate(target.id)}")
    LocalDate getEndDate();

    @Value("#{@movieRepository.getMinPrice(target.id)}")
    Double getMinPrice();

    @Value("#{@movieRepository.getMaxPrice(target.id)}")
    Double getMaxPrice();





}
