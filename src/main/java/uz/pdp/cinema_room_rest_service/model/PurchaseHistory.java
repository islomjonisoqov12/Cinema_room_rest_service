package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
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
