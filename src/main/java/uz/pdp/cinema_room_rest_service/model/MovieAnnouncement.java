package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "movie_announcements")
public class MovieSession extends AbsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    Movie movie;

    @Column(name = "is_active")
    boolean isActive;

    @OneToMany(mappedBy = "movieSession", cascade = CascadeType.ALL)
    List<ReservedHall> reservedHalls;


    public MovieSession(UUID id, Movie movie, boolean isActive) {
        this.id = id;
        this.movie = movie;
        this.isActive = isActive;
    }
}
