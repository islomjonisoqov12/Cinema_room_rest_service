package uz.pdp.cinema_room_rest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data
public class HallDto {
    @NotNull(message = "you cannot add without hall")
    UUID hallId;

    @NotNull(message = "must input start date")
    Date startDate;

    Date endDate;

    @NotNull(message = "select start time for create session")
    List<SessionTimeDto> startTimes;
}
