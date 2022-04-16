package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.cinema_room_rest_service.enums.MovieStatus;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@AllArgsConstructor @NoArgsConstructor @Data
@Entity(name = "movies")
public class Movie extends AbsEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false, columnDefinition = "time")
    Time time;

    @Column(nullable = false)
    Double price = 0.0;

    @Column(nullable = false, name = "releaseDate",columnDefinition = "date")
    LocalDate releaseData;

    @Column(nullable = false)
    Double budget;

    @Column(name = "distributor_share_in_percentage", nullable = false)
    Double distributorShareInPercentage = 0.0;

    @Column(columnDefinition = "text")
    String description;

    @Enumerated(EnumType.STRING)
    MovieStatus status;


    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "trailer_video")
    @OneToOne(cascade = CascadeType.ALL)
    Attachment trailerVideo;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    List<Attachment> posterImg = new ArrayList<>();


    @JoinColumn(name = "distributed_by")
    @ManyToOne(cascade = CascadeType.PERSIST)
    Distributor distributedBy;


    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "movies_countries", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "country_id"))
    List<Country> countries = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "movies_directors", joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id"))
    List<Director> directors = new ArrayList<>();


    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "movies_genres", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    List<Genre> genres = new ArrayList<>();


    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "movies_actors", joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    List<Actor> actors = new ArrayList<>();

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    List<MovieAnnouncement> sessions = new ArrayList<>();


    public Movie(UUID id, String title, Time time, Double price, LocalDate releaseData, Double budget, Double distributorShareInPercentage, String description, Attachment trailerVideo, List<Attachment> posterImg, Distributor distributedBy, List<Country> countries, List<Director> directors, List<Genre> genres, List<Actor> actors) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.price = price;
        this.releaseData = releaseData;
        this.budget = budget;
        this.distributorShareInPercentage = distributorShareInPercentage;
        this.description = description;
        this.trailerVideo = trailerVideo;
        this.posterImg = posterImg;
        this.distributedBy = distributedBy;
        this.countries = countries;
        this.directors = directors;
        this.genres = genres;
        this.actors = actors;
    }
    public Movie(UUID id, String title, Time time, Double price, LocalDate releaseData, Double budget, Double distributorShareInPercentage, String description,MovieStatus status, Attachment trailerVideo, List<Attachment> posterImg, Distributor distributedBy, List<Country> countries, List<Director> directors, List<Genre> genres, List<Actor> actors) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.price = price;
        this.releaseData = releaseData;
        this.budget = budget;
        this.distributorShareInPercentage = distributorShareInPercentage;
        this.description = description;
        this.status = status;
        this.trailerVideo = trailerVideo;
        this.posterImg = posterImg;
        this.distributedBy = distributedBy;
        this.countries = countries;
        this.directors = directors;
        this.genres = genres;
        this.actors = actors;
    }
}
