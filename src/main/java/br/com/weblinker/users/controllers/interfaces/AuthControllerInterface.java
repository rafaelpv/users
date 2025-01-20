package br.com.weblinker.users.controllers.interfaces;

import br.com.weblinker.users.dto.LoginRequest;
import br.com.weblinker.users.dto.RefreshTokenRequest;
import br.com.weblinker.users.dto.TokenResponse;
import br.com.weblinker.users.exceptions.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authentication endpoints", description = "Endpoints for user authentication and token management")
public interface AuthControllerInterface {

    @Operation(
        summary = "User login",
        description = "Authenticates a user with email and password, returning an access token and a refresh token.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User authenticated successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TokenResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Invalid login credentials",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)
                )
            )
        }
    )
    TokenResponse login(@Valid @RequestBody LoginRequest data);

    @Operation(
        summary = "User logout",
        description = "Logs out the user by invalidating their current session, effectively removing the access token and refresh token from the user's context.",
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "User logged out successfully",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Invalid token supplied!",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)
                )
            )
        }
    )
    ResponseEntity<Void> logout(HttpServletRequest request);

    @Operation(
        summary = "Refresh token",
        description = "Generates a new access token using a valid refresh token.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Token refreshed successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TokenResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Invalid or expired refresh token",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)
                )
            )
        }
    )
    TokenResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest);
}
