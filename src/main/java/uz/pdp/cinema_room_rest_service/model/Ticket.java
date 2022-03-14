package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data
@Entity(name = "tickets")
public class Ticket extends AbsEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(nullable = false, name = "qr_code")
    String qrCode;

    @Column(nullable = false)
    Double price = 0.0;

    TicketStatus status;

    @Column(nullable = false, name = "created_at")
    LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    MovieSession session;

    @ManyToOne
    Seat seat;

    @ManyToOne
    Cart cart;


}
