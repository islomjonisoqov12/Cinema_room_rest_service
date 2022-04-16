package uz.pdp.cinema_room_rest_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterDto {
    @NotEmpty(message = "the full name must not be empty")
    @NotBlank(message = "the full name must consist of characters")
    @NotNull(message = "you must input your full name")
    String fullName;

    @NotEmpty(message = "the username must not be empty")
    @NotBlank(message = "the username must consist of characters")
    @Length(min = 5)
    @NotNull(message = "you must input your username")
    String username;

    @NotEmpty(message = "the password must not be empty")
    @NotBlank(message = "the password must consist of characters")
    @NotNull(message = "you must input your password")
    @Length(min = 5)
    String password;

    @NotEmpty(message = "the email must not be empty")
    @NotBlank(message = "the email must consist of characters")
    @NotNull(message = "you must input your email")
    @Email(message = "email not valid")
    String email;

    @NotEmpty(message = "the phone number must not be empty")
    @NotBlank(message = "the phone number must consist of characters")
    @NotNull(message = "you must input your phone number")
    String phoneNumber;

    @NotBlank(message = "the bio must consist of characters")
    String bio;
}
