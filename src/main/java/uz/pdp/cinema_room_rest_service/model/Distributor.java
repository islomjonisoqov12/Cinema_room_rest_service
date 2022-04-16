package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;


@AllArgsConstructor @NoArgsConstructor @Data
@Entity(name = "distributors")
public class Distributor extends AbsEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(nullable = false)
    String name;

    @Column(columnDefinition = "text")
    String description;

    @OneToOne(fetch = FetchType.LAZY)
    Attachment img;

    public Distributor(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
