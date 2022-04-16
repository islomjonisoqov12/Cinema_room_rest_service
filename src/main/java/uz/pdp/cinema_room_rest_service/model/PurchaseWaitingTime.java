package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.time.Duration;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "purchase_waiting_time")
public class PurchaseWaitingTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(columnDefinition = "time")
    Time time;

}
