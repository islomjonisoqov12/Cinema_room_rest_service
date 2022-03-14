package uz.pdp.cinema_room_rest_service.model;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "pay_types")
public class PayType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(nullable = false)
    String name;

    @OneToOne
    Attachment logo;

}
