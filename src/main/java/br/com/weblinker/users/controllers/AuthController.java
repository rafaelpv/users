package br.com.weblinker.users.controllers;

import br.com.weblinker.users.dto.AuthRequest;
import br.com.weblinker.users.dto.AuthResponse;
import br.com.weblinker.users.dto.RefreshRequest;
import br.com.weblinker.users.models.User;
import br.com.weblinker.users.repositories.UsersRepository;
import br.com.weblinker.users.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtUtil jwtUtil;

    //@Autowired
    //private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        // Authenticate user
        User userDetails = usersRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!userDetails.getPassword().equals(authRequest.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // Generate tokens
        String accessToken = jwtUtil.generateAccessToken(userDetails.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getEmail());

        // Store refresh token in Redis
        //refreshTokenService.storeRefreshToken(userDetails.getEmail(), refreshToken);

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }
/*
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        String username = jwtUtil.extractUsername(refreshRequest.getRefreshToken(), true);

        if (!jwtUtil.validateToken(refreshRequest.getRefreshToken(), true)) {
            return ResponseEntity.status(403).body("Invalid refresh token");
        }

        // Verify with Redis
        String cachedToken = refreshTokenService.getRefreshToken(username);
        if (cachedToken == null || !cachedToken.equals(refreshRequest.getRefreshToken())) {
            return ResponseEntity.status(403).body("Invalid or expired refresh token");
        }

        // Generate new access token
        String newAccessToken = jwtUtil.generateAccessToken(username);
        return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshRequest.getRefreshToken()));
    }

 */
}