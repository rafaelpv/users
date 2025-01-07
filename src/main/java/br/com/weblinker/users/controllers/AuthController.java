package br.com.weblinker.users.controllers;

import br.com.weblinker.users.controllers.interfaces.AuthControllerInterface;
import br.com.weblinker.users.dto.LoginRequest;
import br.com.weblinker.users.dto.RefreshTokenRequest;
import br.com.weblinker.users.dto.TokenResponse;
import br.com.weblinker.users.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerInterface {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest data) {
        return authService.login(data);
    }

    @PutMapping("/refresh")
    public TokenResponse refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest
    ) {
        return authService.refreshToken(refreshTokenRequest.getRefreshToken());
    }
}
