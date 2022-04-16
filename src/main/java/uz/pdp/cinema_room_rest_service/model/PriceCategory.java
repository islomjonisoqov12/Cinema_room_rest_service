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
@Entity(name = "price_categories")
public class PriceCategory extends AbsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(unique = true, nullable = false, length = 50)
    String name;

    @Column(name = "additional_fee_in_percentage", nullable = false)
    Double additionalFeeInPercentage = 0.0;

    @OneToMany(mappedBy = "priceCategory")
    List<Seat> seats = new ArrayList<>();

    public PriceCategory(String name, Double additionalFeeInPercentage) {
        this.name = name;
        this.additionalFeeInPercentage = additionalFeeInPercentage;
    }
    public PriceCategory(UUID id ,String name, Double additionalFeeInPercentage) {
        this.id = id;
        this.name = name;
        this.additionalFeeInPercentage = additionalFeeInPercentage;
    }
}
