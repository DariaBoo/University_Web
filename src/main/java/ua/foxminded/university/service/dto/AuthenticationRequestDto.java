package ua.foxminded.university.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticationRequestDto {

    @NotBlank(message = "Username must not be null or empty")
    @Size(max = 30, message = "Username size must be less then 30 symbols")
    private String username;

    @NotBlank(message = "Password must not be null or empty")
    @Size(max = 20, message = "Password size must be less then 20 symbols")
    private String password;
}
