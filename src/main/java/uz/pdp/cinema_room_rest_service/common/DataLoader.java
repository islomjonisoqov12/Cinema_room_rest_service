package uz.pdp.cinema_room_rest_service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.pdp.cinema_room_rest_service.model.*;
import uz.pdp.cinema_room_rest_service.repository.*;

import javax.transaction.Transactional;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@Transactional
@Component
public class DataLoader implements CommandLineRunner {

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

    @Autowired
    AttachmentRepository attachmentRepository;


    @Value("@{spring.sql.init.mode}")
    String initMode;

    @Override
    public void run(String... args) throws Exception {

            Actor actor = new Actor(null, "Islomjon Isoqov", null, "bio");
            Actor actor1 = new Actor(null, "Shoxrux Ergashev", null, "bio");

            actor = actorRepository.save(actor1);
            actor1 = actorRepository.save(actor);

            Genre genre = new Genre(null, "horror");
            Genre genre1 = new Genre(null, "Drama");
            Genre genre2 = new Genre(null, "Documentary");

            genre = genreRepository.save(genre);
            genre1 = genreRepository.save(genre1);
            genre2 = genreRepository.save(genre2);


            Director director = new Director(null, "Isoqov Islom", "bio..");
            director =  directorRepository.save(director);

            Country country = new Country(null, "Uzbekistan", "ksdjaskdf");
            country =  countryRepository.save(country);
            Distributor distributor = new Distributor(null, "warner bros", "description");
            distributor=distributorRepository.save(distributor);

            Attachment video = new Attachment(null, "file.mp4", "video/mp4", 1230000);
            video = attachmentRepository.save(video);
            Attachment img = new Attachment(null, "file.jpg", "image/jpg", 12300);
            img = attachmentRepository.save(img);

        Movie movie = new Movie(
                    null,
                    "romeo and julep",
                    new Time(1, 30, 0),
                    20000.0,
                    new Date(),
                    12000000.0,
                    60.0,
                    "description",
                    video,
                    img,
                    distributor,
                    new ArrayList<>(Collections.singletonList(country)),
                    new ArrayList<>(Collections.singletonList(director)),
                    new ArrayList<>(Arrays.asList(genre, genre1, genre2)),
                    new ArrayList<>(Arrays.asList(actor, actor1)),
                    new ArrayList<>()
            );
            movieRepository.save(movie);
    }
}
