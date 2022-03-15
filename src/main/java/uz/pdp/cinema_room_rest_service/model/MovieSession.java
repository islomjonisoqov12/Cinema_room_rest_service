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
@Entity(name = "movie_sessions")
public class MovieSession extends AbsEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(nullable = false)
    Date date;

    @Column(nullable = false)
    Time time;

    @Column(name = "session_additional_fee_in_percent", nullable = false)
    Double sessionAdditionalFeeInPercent = 0.0;

    @ManyToOne
    Movie movie;

    @ManyToOne
    Hall hall;

    @OneToMany(mappedBy = "session")
    List<Ticket> tickets = new ArrayList<>();



}
