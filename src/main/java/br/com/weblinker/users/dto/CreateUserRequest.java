package br.com.weblinker.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateUserRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 100, message = "First name should have between 3 and 100 chars")
    @Schema(description = "First name of the user", example = "John")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 100, message = "Last name should have between 3 and 100 chars")
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    @NotBlank(message = "E-mail is required")
    @Email(message = "Invalid e-mail")
    @Schema(description = "User's e-mail", example = "john@doe.com")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(min = 3, max = 20, message = "Phone number should have between 3 and 20 chars")
    @Schema(description = "User's phone", example = "+34999888777")
    private String phone;
}
