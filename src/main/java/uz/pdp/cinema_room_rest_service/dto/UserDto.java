package uz.pdp.cinema_room_rest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    @NotEmpty(message = "the username must not be empty")
    @NotBlank(message = "the username must consist of characters")
    @NotNull(message = "you must input your username")
    String username;

    @NotEmpty(message = "the password must not be empty")
    @NotBlank(message = "the password must consist of characters")
    @NotNull(message = "you must input your password")
    String password;
}
