package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "pay_types")
public class PayType extends AbsEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(nullable = false)
    String name;

    @OneToOne
    Attachment logo;

}
