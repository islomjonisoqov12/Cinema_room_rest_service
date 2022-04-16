package uz.pdp.cinema_room_rest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class RowDto {

    @Range(min = 1, message = "row number starts from 1 The 0th row will not be")
    int rowNumber;

    @NotEmpty(message = "input seats for add a new row")
    List<SeatDto> seatDtoList;
}
