package uz.pdp.cinema_room_rest_service.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @OneToOne
    User user;

    @OneToMany(mappedBy = "cart")
    List<Ticket> tickets = new ArrayList<>();


}
