package uz.pdp.cinema_room_rest_service.model;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "refund_charge_fees")
public class RefundChargeFee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(nullable = false)
    Integer intervalInMinutes;

    @Column(nullable = false)
    Double chargeFeeInPercentage = 0.0;
}
