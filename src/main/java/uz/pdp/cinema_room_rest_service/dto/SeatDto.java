package uz.pdp.cinema_room_rest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SeatDto {
    @NotNull(message = "you don't select any price category. Please select")
    UUID priceCategoryId;

    @Range(min = 1, message = "seat number starts from 1 The 0th seat will not be")
    int beginNumber;

    @Range(min = 1, message = "seat number starts from 1 the 0 th seat will not be")
    int endNumber;
}
