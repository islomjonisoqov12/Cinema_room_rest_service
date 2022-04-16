package uz.pdp.cinema_room_rest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionTimeDto {

    @NotNull(message = "you cannot add session without start time")
    @NotEmpty(message = "the start time must not be empty")
    @NotBlank(message = "the start time must consist of number")
    String startTime;

    String  endTime;

    double sessionAdditionalFeeInPer;

    public Time getStartTime() {
        return Time.valueOf(startTime);
    }

    public Time getEndTime(){
        return Time.valueOf(endTime);
    }
}
