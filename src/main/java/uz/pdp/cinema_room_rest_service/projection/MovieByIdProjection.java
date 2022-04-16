package uz.pdp.cinema_room_rest_service.projection;


import org.springframework.beans.factory.annotation.Value;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public interface MovieByIdProjection {

    String getId();

    String getTitle();

    Time getDuration();

    LocalDate getReleaseDate();

    Double getBudget();

    String getDescription();

    String getImg();

    String getTrailer();

    @Value("#{@genreRepository.getFindByMovieId(target.id)}")
    List<GenreProjection> getGenres();

    @Value("#{@actorRepository.getActorByMovieId(target.id)}")
    List<ActorProjection> getActors();

    @Value("#{@directorRepository.getFindByMovieId(target.id)}")
    List<ActorProjection> getDirectors();



}
