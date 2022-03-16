package uz.pdp.cinema_room_rest_service.model;

import javax.persistence.*;
import java.sql.Time;
import java.util.UUID;

@Entity(name = "reserved_halls")
public class ReservedHalls {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    Hall hall;

    @ManyToOne
    SessionDates date;

    @ManyToOne
    SessionTimes startTime;

    @Column(columnDefinition = "time")
    Time endTime;

    @ManyToOne
    Afisha afisha;
}
