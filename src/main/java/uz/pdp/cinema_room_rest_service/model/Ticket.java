package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.cinema_room_rest_service.enums.TicketStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data
@Entity(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(nullable = false, name = "qr_code")
    String qrCode;

    @Column(nullable = false)
    Double price = 0.0;

    @Enumerated(EnumType.STRING)
    TicketStatus status;

    @Column(nullable = false, name = "created_at")
    LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    boolean checked = false;

    @ManyToOne
    MovieSession movieSession;

    @ManyToOne
    Seat seat;

    @ManyToOne
    Cart cart;



}
