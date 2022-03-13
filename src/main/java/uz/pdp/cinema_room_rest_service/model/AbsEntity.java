package uz.pdp.cinema_room_rest_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor @NoArgsConstructor @Data
public abstract class AbsEntity {
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private User created_by;
    private User updated_by;

}
