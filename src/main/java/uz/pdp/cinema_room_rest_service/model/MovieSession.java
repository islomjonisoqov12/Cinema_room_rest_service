package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;
import java.util.UUID;
@AllArgsConstructor @NoArgsConstructor @Data
@Entity(name = "movie_sessions")
public class ReservedHall {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    Hall hall;

    @ManyToOne(fetch = FetchType.LAZY)
    SessionDates date;

    @ManyToOne(fetch = FetchType.LAZY)
    SessionTime startTime;

    @Column(columnDefinition = "time")
    Time endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    MovieSession movieSession;

    @OneToMany(mappedBy = "reservedHall", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Ticket> tickets;
}
