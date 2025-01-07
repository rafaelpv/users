package br.com.weblinker.users.controllers;

import br.com.weblinker.users.dto.LoginRequest;
import br.com.weblinker.users.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Authenticates a user and returns a token")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest data) {
        if (data == null || data.getUsername() == null || data.getUsername().isBlank() ||
                data.getPassword() == null || data.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid client request");
        }
        return authService.login(data);
    }

    @Operation(summary = "Refresh token for authenticated user and returns a token")
    @PutMapping("/refresh/{username}")
    public ResponseEntity<?> refreshToken(
            @PathVariable("username") String username,
            @RequestHeader("Authorization") String refreshToken) {

        if (refreshToken == null || refreshToken.isBlank() ||
                username == null || username.isBlank()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid client request");
        }
        return authService.refreshToken(username, refreshToken);
    }
}
