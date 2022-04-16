package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "hall_rows")
public class Row extends AbsEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(nullable = false)
    int number;

    @ManyToOne
    Hall hall;

    @OneToMany(mappedBy = "row")
    List<Seat> seats = new ArrayList<>();

    public Row(UUID id, int number, Hall hall) {
        this.id = id;
        this.number = number;
        this.hall = hall;
    }

    public void addSeats(Seat seat) {
        seats.add(seat);
    }
}
