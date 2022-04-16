package uz.pdp.cinema_room_rest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HallDateForBusyDateDto {
    @NotNull(message = "give the session start time to search")
    Date start;
    Date end;
    @NotNull(message = "you must input hall id")
    UUID hallId;

    public HallDateForBusyDateDto(Date start, UUID hallId) {
        this.start = start;
        this.hallId = hallId;
    }
}


