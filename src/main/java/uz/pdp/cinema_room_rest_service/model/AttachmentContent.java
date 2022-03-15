package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data
@Entity(name = "attachment_contents")
public class AttachmentContent {
    @Id
    @GeneratedValue
    private UUID id;

    private byte [] data;

    @OneToOne(cascade = CascadeType.ALL)
    Attachment attachment;
}
