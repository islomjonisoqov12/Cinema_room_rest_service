package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data
@Entity(name = "movie_sessions")
public class Afisha extends AbsEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @ManyToOne
    Movie movie;

    @Column(name = "is_active")
    boolean isActive;

    @OneToMany(mappedBy = "session")
    List<Ticket> tickets = new ArrayList<>();

    public Afisha(UUID id, Movie movie, boolean isActive) {
        this.id = id;
        this.movie = movie;
        this.isActive = isActive;
    }
}
