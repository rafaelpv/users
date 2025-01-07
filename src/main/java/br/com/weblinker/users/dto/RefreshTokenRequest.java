package br.com.weblinker.users.dto;

import br.com.weblinker.users.validations.ValidRefreshToken;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token is required")
    @ValidRefreshToken
    @Schema(description = "Refresh token", example = "eyui..")
    private String refreshToken;
}
