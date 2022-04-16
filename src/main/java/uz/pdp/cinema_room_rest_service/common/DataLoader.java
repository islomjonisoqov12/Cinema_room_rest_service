package uz.pdp.cinema_room_rest_service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.cinema_room_rest_service.model.Role;
import uz.pdp.cinema_room_rest_service.model.User;
import uz.pdp.cinema_room_rest_service.repository.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;


@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    MovieRepository movieRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    PriceCategoryRepository priceCategoryRepo;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    RowRepository rowRepository;
    @Autowired
    HallRepository hallRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    SessionDateRepository sessionDateRepository;
    @Autowired
    SessionTimesRepository sessionTimesRepository;
    @Autowired
    MovieSessionRepository movieSessionRepository;



    @Value("${spring.sql.init.mode}")
    String init_mode;

//    public boolean check(){
//
//        if (!ddl_auto.equals("update")) {
//            System.out.println("rostdan ham o'chirishni xohlaysanmi?");
//            String s = new Scanner(System.in).nextLine();
//            if (!s.equals("ok")) {
//                return false;
//            }else
//                return true;
//        }
//        return true;
//    }

    @Override
    public void run(String... args) throws Exception {
        if (init_mode.equals("always")) {
            for (User user : userRepository.findAll()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }
        }
    }
//    {
//
//        boolean check = check();
//        if (!check) {
//            throw new NullPointerException();
//        }
//        if (!ddl_auto.equals("update")) {
//
//            Actor actor = new Actor(null, "Islomjon Isoqov", null, "bio", null);
//            Actor actor1 = new Actor(null, "Shoxrux Ergashev", null, "bio", null);
//
//            actor = actorRepository.save(actor);
//            actor1 = actorRepository.save(actor1);
//
//            Genre genre = new Genre(null, "horror");
//            Genre genre1 = new Genre(null, "Drama");
//            Genre genre2 = new Genre(null, "Documentary");
//
//            genre = genreRepository.save(genre);
//            genre1 = genreRepository.save(genre1);
//            genre2 = genreRepository.save(genre2);
//
//            List<Hall> hallList = new ArrayList<>(Arrays.asList(
//                    new Hall("Zal 1", 0.0),
//                    new Hall("Zal 2", 5.0),
//                    new Hall("Zal 3", 0.0),
//                    new Hall("Zal 4", 0.0)
//            ));
//            hallList = hallRepository.saveAll(hallList);
//
//
//            Director director = new Director(null, "Isoqov Islom", "bio..");
//            director = directorRepository.save(director);
//
//            Country country = new Country(null, "Uzbekistan", "ksdjaskdf");
//            country = countryRepository.save(country);
//            Distributor distributor = new Distributor(null, "warner bros", "description");
//            distributor = distributorRepository.save(distributor);
//
//            Attachment video = new Attachment(null, "file.mp4", "video/mp4", 1230000);
//            video = attachmentRepository.save(video);
//            Attachment img = new Attachment(null, "file.jpg", "image/jpg", 12300);
//            img = attachmentRepository.save(img);
//
//            Movie movie = new Movie(
//                    null,
//                    "romeo and julep",
//                    new Time(1, 40, 0),
//                    20000.0,
//                    new Date(),
//                    12000000.0,
//                    60.0,
//                    "description",
//                    MovieStatus.ACTIVE,
//                    video,
//                    img,
//                    distributor,
//                    new ArrayList<>(Collections.singletonList(country)),
//                    new ArrayList<>(Collections.singletonList(director)),
//                    new ArrayList<>(Arrays.asList(genre, genre1, genre2)),
//                    new ArrayList<>(Arrays.asList(actor, actor1)),
//                    new ArrayList<>()
//            );
//            movieRepository.save(movie);
//
//            PriceCategory cat1 = priceCategoryRepo.save(new PriceCategory(null, "KATEGORIYA 1", 10.0, null));
//            PriceCategory cat2 = priceCategoryRepo.save(new PriceCategory(null, "KATEGORIYA 2", 8.0, null));
//
//            // JOYLARNI DBGA QO'SHISH (QATORLARINI HAM BIRGALIKDA)
//
//
//            Hall hall1 = hallRepository.findByName("Zal 1");
//            Hall hall2 = hallRepository.findByName("Zal 2");
//
//            Row row1 = rowRepository.save(new Row(null, 1, hall1));
//            Row row2 = rowRepository.save(new Row(null, 2, hall1));
//            Row row3 = rowRepository.save(new Row(null, 3, hall1));
//            Row row12 = rowRepository.save(new Row(null, 1, hall2));
//
//            List<Seat> seatList = new ArrayList<>(Arrays.asList(
//                    new Seat(null, 1, row1, cat2),
//                    new Seat(null, 2, row1, cat2),
//                    new Seat(null, 3, row1, cat2),
//                    new Seat(null, 4, row1, cat1),
//                    new Seat(null, 5, row1, cat1),
//                    new Seat(null, 6, row1, cat1),
//                    new Seat(null, 7, row1, cat1),
//                    new Seat(null, 8, row1, cat2),
//                    new Seat(null, 9, row1, cat2),
//                    new Seat(null, 10, row1, cat2),
//                    new Seat(null, 1, row2, cat2),
//                    new Seat(null, 2, row2, cat2),
//                    new Seat(null, 3, row2, cat2),
//                    new Seat(null, 4, row2, cat1),
//                    new Seat(null, 5, row2, cat1),
//                    new Seat(null, 6, row2, cat1),
//                    new Seat(null, 7, row2, cat1),
//                    new Seat(null, 8, row2, cat2),
//                    new Seat(null, 9, row2, cat2),
//                    new Seat(null, 10, row2, cat2),
//                    new Seat(null, 1, row3, cat2),
//                    new Seat(null, 2, row3, cat2),
//                    new Seat(null, 3, row3, cat2),
//                    new Seat(null, 4, row3, cat1),
//                    new Seat(null, 5, row3, cat1),
//                    new Seat(null, 6, row3, cat1),
//                    new Seat(null, 7, row3, cat1),
//                    new Seat(null, 8, row3, cat2),
//                    new Seat(null, 9, row3, cat2),
//                    new Seat(null, 10, row3, cat2),
//                    new Seat(null, 1, row12, cat2),
//                    new Seat(null, 2, row12, cat2),
//                    new Seat(null, 3, row12, cat2),
//                    new Seat(null, 4, row12, cat1),
//                    new Seat(null, 5, row12, cat1),
//                    new Seat(null, 6, row12, cat1),
//                    new Seat(null, 7, row12, cat1),
//                    new Seat(null, 8, row12, cat2),
//                    new Seat(null, 9, row12, cat2),
//                    new Seat(null, 10, row12, cat2)
//
//            ));
//
//            seatRepository.saveAll(seatList);
//
//            MovieSession movieSession = new MovieSession(
//                    null,
//                    movie,
//                    true
//            );
//            SessionDates dates = new SessionDates(null, new Date());
//            sessionDateRepository.save(dates);
//
//            SessionTimes sessionTimes = new SessionTimes(null, new Time(1440), 4.0);
//            sessionTimeRepository.save(sessionTimes);
//
//            ReservedHall reservedHall = new ReservedHall(
//                    null,
//                    hall1,
//                    dates,
//                    sessionTimes,
//                    new Time(5, 20, 0),
//                    movieSession,
//                    new ArrayList<>()
//            );
//            reservedHallRepository.save(reservedHall);
//
//
//            session.save(movieSession);
//        }
//
//    }
}
