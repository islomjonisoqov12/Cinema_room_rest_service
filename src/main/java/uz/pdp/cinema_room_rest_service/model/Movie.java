package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
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

    @Column(nullable = false)
    Time time;

    @Column(nullable = false)
    Double price = 0.0;

    @Column(nullable = false, name = "releaseDate")
    Date releaseData;

    @Column(nullable = false)
    Double budget;

    @Column(name = "distributor_share_in_percentage", nullable = false)
    Double distributorShareInPercentage = 0.0;

    @Column(columnDefinition = "text")
    String description;


    @JoinColumn(name = "trailer_video")
    @OneToOne(cascade = CascadeType.ALL)
    Attachment trailerVideo;

    @JoinColumn(name = "poster_img")
    @OneToOne(cascade = CascadeType.ALL)
    Attachment posterImg;

    @JoinColumn(name = "distributed_by")
    @ManyToOne(cascade = CascadeType.ALL)
    Distributor distributedBy;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "movies_countries", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "country_id"))
    List<Country> countries = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "movies_directors", joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id"))
    List<Director> directors = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "movies_genres", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    List<Genre> genres = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "movies_actors", joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    List<Actor> actors = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    List<MovieSession> sessions = new ArrayList<>();


    public Movie(UUID id, String title, Time time, Double price, Date releaseData, Double budget, Double distributorShareInPercentage, String description, Attachment trailerVideo, Attachment posterImg, Distributor distributedBy, List<Country> countries, List<Director> directors, List<Genre> genres, List<Actor> actors) {
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
}
