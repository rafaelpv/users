package br.com.weblinker.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    @Schema(description = "User's ID", example = "1")
    private Long id;
    @Schema(description = "First name of the user", example = "John")
    private String firstName;
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;
    @Schema(description = "User's e-mail", example = "john@doe.com")
    private String email;
    @Schema(description = "User's phone", example = "+34999888777")
    private String phone;
}
