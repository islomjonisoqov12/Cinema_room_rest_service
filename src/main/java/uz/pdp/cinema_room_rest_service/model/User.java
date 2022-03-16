package uz.pdp.cinema_room_rest_service.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    @Column(name = "full_name", nullable = false)
    String fullName;
    @Column(unique = true, nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    @Column(unique = true, nullable = false)
    String email;

    @Column(name = "phone_number", unique = true, nullable = false)
    String phoneNumber;

    @Column(columnDefinition = "text")
    String bio;

    @OneToMany(mappedBy = "user")
    List<BillingInfo> billingInfos = new ArrayList<>();
}
