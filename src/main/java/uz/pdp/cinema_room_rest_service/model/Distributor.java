package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;


@AllArgsConstructor @NoArgsConstructor @Data
@Entity(name = "distributors")
public class Distributor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(nullable = false)
    String name;

    @Column(columnDefinition = "text")
    String description;
}