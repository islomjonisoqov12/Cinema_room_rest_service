package uz.pdp.cinema_room_rest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionDto {
    @NotNull(message = "you must select movie announcement")
    UUID movieAnnouncementId;
    @NotNull(message = "you must select hall")
    UUID hallId;
    @NotNull(message = "please choose the session begin time")
    String startDateTime;

    String  endDateTime;

    public Timestamp getStartDateTime() {
        return Timestamp.valueOf(startDateTime);
    }

    public Timestamp getEndDateTime() {
        return Timestamp.valueOf(endDateTime);
    }
}
