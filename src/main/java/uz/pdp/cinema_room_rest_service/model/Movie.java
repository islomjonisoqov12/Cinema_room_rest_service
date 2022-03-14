package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@AllArgsConstructor @NoArgsConstructor @Data
@Entity(name = "movies")
public class Movie {
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
    @OneToOne
    Attachment trailerVideo;

    @JoinColumn(name = "poster_img")
    @OneToOne
    Attachment posterImg;

    @JoinColumn(name = "distributed_by")
    @ManyToOne
    Distributor distributedBy;

    @ManyToMany
    @JoinTable(name = "movies_countries", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "country_id"))
    List<Country> countries;

    @ManyToMany
    List<Director> directors;

    @ManyToMany
    @JoinTable(name = "movies_genres", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    List<Genre> genres;

    @ManyToMany
    @JoinTable(name = "movies_actors", joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    List<Actor> actors;



}
