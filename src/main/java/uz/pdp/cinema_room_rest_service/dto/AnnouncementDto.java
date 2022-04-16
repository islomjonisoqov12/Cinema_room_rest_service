package uz.pdp.cinema_room_rest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data
public class AnnouncementDto {
    @NotNull(message = "you need select movie for create session")
    UUID movieId;

    @NotNull(message = "input in which halls it will be seats")
    List<HallDto> halls;

    boolean active;

}
