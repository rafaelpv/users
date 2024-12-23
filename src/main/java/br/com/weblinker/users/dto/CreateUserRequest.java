package br.com.weblinker.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
            message = "A senha deve ter pelo menos 8 caracteres, uma letra maiúscula, uma letra minúscula, um número e um caractere especial.")
    @Schema(description = "User's password", example = "MyPassword1234*")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Size(min = 3, max = 20, message = "Phone number should have between 3 and 20 chars")
    @Schema(description = "User's phone", example = "+34999888777")
    private String phone;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }
}
