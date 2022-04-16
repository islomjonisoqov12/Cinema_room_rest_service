package uz.pdp.cinema_room_rest_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.cinema_room_rest_service.dto.MovieDto;
import uz.pdp.cinema_room_rest_service.enums.MovieStatus;
import uz.pdp.cinema_room_rest_service.model.*;
import uz.pdp.cinema_room_rest_service.payload.ApiResponse;
import uz.pdp.cinema_room_rest_service.projection.MovieByIdProjection;
import uz.pdp.cinema_room_rest_service.projection.MovieProjection;
import uz.pdp.cinema_room_rest_service.repository.*;

import javax.transaction.Transactional;
import java.sql.Time;
import java.util.*;

@Transactional
@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    MovieAnnouncementRepository session;

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


    public List<MovieAnnouncement> getSessionById(UUID movieId) {
        return session.findBYMovieId(movieId);
    }


    public ApiResponse getMovieById(UUID movieId) {
        ApiResponse response = new ApiResponse();
        try {
            List<MovieByIdProjection> movieByMovieId = movieRepository.getMovieByMovieId(movieId);
            response.setMessage("success");
            response.setData(movieByMovieId);
            response.setSuccess(true);
        }catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public boolean saveMovie(MovieDto movieDto, ApiResponse response) {

        try {
            List<Actor> actors = new ArrayList<>();
            List<Genre> genres = new ArrayList<>();
            List<Director> directors = new ArrayList<>();
            List<Country> countries = new ArrayList<>();
            Movie movie = new Movie();

            if(movieDto.getId()!=null){
                movie.setId(movieDto.getId());
            }
            movie.setTitle(movieDto.getTitle());
            movie.setTime(new Time(movieDto.getTime()));
            movie.setReleaseData(movieDto.getReleaseDate().toLocalDate());
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

            List<Attachment> img = new ArrayList<>();
            for (MultipartFile postImage : movieDto.getPostImages()) {
                Attachment attachment = saveAttachment(postImage);
                img.add(attachment);
            }
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
            movie.setStatus(MovieStatus.valueOf(movieDto.getStatus()));

            movie.setDistributorShareInPercentage(movieDto.getDistributorShareInPercentage());

            Optional<Distributor> distributor = distributorRepository.findById(movieDto.getDistributor());
            distributor.ifPresent(movie::setDistributedBy);

            movieRepository.save(movie);
            response.setMessage("success");
            response.setSuccess(true);
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


    public ApiResponse getMovies(ApiResponse response, int page, int size) {
        List<MovieProjection> movies = movieRepository.getMovies(PageRequest.of(page, size));
        if(movies.size()>0){
            response.setSuccess(true);
            response.setMessage("success");
            response.setData(movies);
        }
        return response;
    }
    public ApiResponse deleteMovie(UUID id) {
        ApiResponse response = new ApiResponse();
        try {
            movieRepository.deleteById(id);
            response.setSuccess(true);
            response.setMessage("successfully deleted");
        }catch (Exception e){
            System.out.println(e.getMessage());
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }
        return response;
    }
}
