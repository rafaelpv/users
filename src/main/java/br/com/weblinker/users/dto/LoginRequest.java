package br.com.weblinker.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 100, message = "Username should have between 3 and 100 chars")
    @Schema(description = "Username of the user", example = "johndoe")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 3, max = 100, message = "Password should have between 3 and 100 chars")
    @Schema(description = "Password of the user", example = "123456")
    private String password;
}
