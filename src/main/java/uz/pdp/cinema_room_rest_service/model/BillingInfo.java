package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data
@Entity(name = "billing_infos")
public class BillingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    @Column(name = "card_holder_name")
    String cardHolderName;
    @Column(name = "card_number")
    String cardNumber;
    @Column(name = "expiration_month")
    Integer expirationMonth;
    @Column(name = "expiration_year")
    Integer expirationYear;
    @Column(name = "cvc_code")
    String cvcCode;

    @ManyToOne
    User user;

}
