package uz.pdp.cinema_room_rest_service.model;

import javax.persistence.*;
import java.time.Duration;
import java.util.UUID;

@Entity(name = "purchase_waiting_time")
public class PurchaseWaitingTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(columnDefinition = "time")
    Duration time;

}
