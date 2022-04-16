package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "movie_announcements")
public class MovieAnnouncement extends AbsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.EAGER)
    Movie movie;

    @Column(name = "is_active")
    boolean isActive;

    @OneToMany(mappedBy = "movieAnnouncement", cascade = CascadeType.ALL)
    List<MovieSession> movieSessions = new ArrayList<>();


    public MovieAnnouncement(UUID id, Movie movie, boolean isActive) {
        this.id = id;
        this.movie = movie;
        this.isActive = isActive;
    }

    public void addMovieSession(MovieSession movieSession) {
        movieSessions.add(movieSession);
    }
}
