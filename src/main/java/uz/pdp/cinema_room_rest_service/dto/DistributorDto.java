package uz.pdp.cinema_room_rest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DistributorDto {
    UUID id;
    @NotEmpty(message = "the name must not be empty")
    @NotBlank(message = "the name must consist of characters")
    @NotNull(message = "you must input your full name")
    String name;

    @NotBlank(message = "the description must consist of characters")
    String description;
    MultipartFile img;

}
