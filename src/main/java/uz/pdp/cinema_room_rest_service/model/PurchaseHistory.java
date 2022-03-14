package uz.pdp.cinema_room_rest_service.model;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "purchase_histories")
public class PurchaseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    @ManyToOne
    User user;

    @OneToOne
    Ticket ticket;

    @ManyToOne
    PayType payType;
}
