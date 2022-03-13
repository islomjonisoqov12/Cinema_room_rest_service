package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "halls")
public class Hall extends AbsEntity {
    @Id
    @GeneratedValue
    UUID id;

    @Column(length = 50)
    String name;

    @Column(name = "vip_additional_fee_in_percentage", nullable = false)
    Double vipAdditionalFeeInPercentage = 0.0;


    @OneToMany(mappedBy = "hall")
    List<Row> rows;

}
