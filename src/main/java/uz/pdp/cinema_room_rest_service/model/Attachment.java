package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data
@Entity(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(name = "original_file_name", nullable = false)
    String originalFileName;

    @Column(nullable = false, name = "content_type")
    String contentType;
    @Column(nullable = false)
    long size;




}
