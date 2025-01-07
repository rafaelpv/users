package br.com.weblinker.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
@Schema(description = "Response object containing token information for authentication and authorization")
public class TokenResponse {

    @Schema(description = "Username associated with the token", example = "john_doe")
    private String username;

    @Schema(description = "Indicates whether the user is authenticated", example = "true")
    private Boolean authenticated;

    @Schema(description = "Token creation date and time", example = "2025-01-07T14:30:00.000Z")
    private Date created;

    @Schema(description = "Token expiration date and time", example = "2025-01-07T15:30:00.000Z")
    private Date expiration;

    @Schema(description = "Access token for authorization purposes", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "Refresh token for generating a new access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;
}
