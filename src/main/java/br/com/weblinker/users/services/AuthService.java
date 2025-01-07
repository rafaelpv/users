package br.com.weblinker.users.services;

import br.com.weblinker.users.dto.LoginRequest;
import br.com.weblinker.users.dto.TokenResponse;
import br.com.weblinker.users.exceptions.UnauthorizedAccessException;
import br.com.weblinker.users.repositories.UsersRepository;
import br.com.weblinker.users.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UsersRepository usersRepository;

    private final Logger logger = Logger.getLogger(AuthService.class.getName());

    public ResponseEntity<?> login(LoginRequest data) {
        logger.info("Trying to log user " + data.getUsername());
        try {
            String username = data.getUsername();
            String password = data.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            var user = usersRepository.findByUsername(username);
            if (user != null) {
                TokenResponse tokenResponse = tokenProvider.createAccessToken(username, user.getRoles(), user.getCompany().getId());
                return ResponseEntity.ok(tokenResponse);
            } else {
                throw new UsernameNotFoundException("Username " + username + " not found!");
            }
        } catch (AuthenticationException e) {
            throw new UnauthorizedAccessException("Invalid username or password supplied!");
        }
    }

    public ResponseEntity<?> refreshToken(String username, String refreshToken) {
        logger.info("Trying to get refresh token for user " + username);

        var user = usersRepository.findByUsername(username);
        if (user != null) {
            TokenResponse tokenResponse = tokenProvider.refreshToken(refreshToken);
            return ResponseEntity.ok(tokenResponse);
        } else {
            throw new UnauthorizedAccessException("Username " + username + " not found!");
        }
    }
}
