package uz.pdp.cinema_room_rest_service.model;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "cash_box")
public class CashBox {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    Double balance = 0.0;
}
