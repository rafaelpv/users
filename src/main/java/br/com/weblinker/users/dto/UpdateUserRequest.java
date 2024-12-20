package br.com.weblinker.users.dto;

import br.com.weblinker.users.validations.UpdateUserAtLeastOneField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@UpdateUserAtLeastOneField(message = "At least one field (firstName, lastName, email, or phone) must be provided")
public class UpdateUserRequest {

    @Valid
    @Size(min = 3, max = 100, message = "First name should have between 3 and 100 chars")
    @Schema(description = "First name of the user", example = "John")
    private String firstName;

    @Valid
    @Size(min = 3, max = 100, message = "Last name should have between 3 and 100 chars")
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    @Valid
    @Email(message = "Invalid e-mail")
    @Schema(description = "User's e-mail", example = "john@doe.com")
    private String email;

    @Valid
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

    public String getPhone() {
        return phone;
    }
}
