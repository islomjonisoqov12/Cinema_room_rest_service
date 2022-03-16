package uz.pdp.cinema_room_rest_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.cinema_room_rest_service.dto.MovieDto;
import uz.pdp.cinema_room_rest_service.model.*;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.repository.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    MovieSessionRepository session;

    @Autowired
    ActorRepository actorRepository;

    @Autowired
    AttachmentContentRepository contentRepository;

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    DistributorRepository distributorRepository;

    @Autowired
    DirectorRepository directorRepository;


    public List<MovieSession> getSessionById(UUID movieId) {
        return session.findBYMovieId(movieId);
    }

    public Movie getMovieById(UUID movieId) {
        return movieRepository.getById(movieId);
    }

    public boolean saveMovie(MovieDto movieDto, ApiResponse response) {
        try {
            List<Actor> actors = new ArrayList<>();
            List<Genre> genres = new ArrayList<>();
            List<Director> directors = new ArrayList<>();
            List<Country> countries = new ArrayList<>();
            Movie movie = new Movie();
            movie.setTitle(movieDto.getTitle());
            movie.setTime(movieDto.getTime());
            for (UUID actorId : movieDto.getActors()) {
                Optional<Actor> actor = actorRepository.findById(actorId);
                actor.ifPresent(actors::add);
            }
            if (actors.size() == 0) throw new Exception("no any actors");
            movie.setActors(actors);
            movie.setBudget(movieDto.getBudget());
            movie.setDescription(movieDto.getDescription());
            movie.setPrice(movieDto.getPrice());

            Attachment video = saveAttachment(movieDto.getVideo());
            movie.setTrailerVideo(video);

            Attachment img = saveAttachment(movieDto.getPostImg());
            movie.setPosterImg(img);

            for (UUID countryId : movieDto.getCountries()) {
                Optional<Country> country = countryRepository.findById(countryId);
                country.ifPresent(countries::add);
            }
            if (actors.size() == 0) throw new Exception("no any country");
            movie.setCountries(countries);

            for (UUID genreId : movieDto.getGenres()) {
                Optional<Genre> genre = genreRepository.findById(genreId);
                genre.ifPresent(genres::add);
            }
            if (actors.size() == 0) throw new Exception("no any country");
            movie.setGenres(genres);

            for (UUID directorId : movieDto.getDirectors()) {
                Optional<Director> director = directorRepository.findById(directorId);
                director.ifPresent(directors::add);
            }
            if (actors.size() == 0) throw new Exception("no any country");
            movie.setDirectors(directors);

            movie.setDistributorShareInPercentage(movieDto.getDistributorShareInPercentage());

            Optional<Distributor> distributor = distributorRepository.findById(movieDto.getDistributor());
            distributor.ifPresent(movie::setDistributedBy);

            movieRepository.save(movie);
            return true;

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return false;
        }
    }

    public Attachment saveAttachment(MultipartFile file) {
        try {
            Attachment attachment = new Attachment();
            AttachmentContent content = new AttachmentContent();
            content.setData(file.getBytes());
            attachment.setContentType(file.getContentType());
            attachment.setSize(file.getSize());
            attachment.setOriginalFileName(file.getOriginalFilename());
            content.setAttachment(attachment);
            contentRepository.save(content);
            return attachment;
        } catch (Exception e) {
            return null;
        }
    }
}
