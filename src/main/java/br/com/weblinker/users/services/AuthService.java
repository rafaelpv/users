package br.com.weblinker.users.services;

import br.com.weblinker.users.dto.LoginRequest;
import br.com.weblinker.users.dto.TokenResponse;
import br.com.weblinker.users.exceptions.UnauthorizedAccessException;
import br.com.weblinker.users.repositories.UsersRepository;
import br.com.weblinker.users.security.jwt.JwtTokenProvider;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public TokenResponse login(LoginRequest data) {
        logger.info("Trying to log user " + data.getEmail());
        try {
            String username = data.getEmail();
            String password = data.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            var user = usersRepository.findByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException("Username " + username + " not found!");
            }

            return tokenProvider.createAccessToken(username, user.getRoles(), user.getCompany().getId());
        } catch (AuthenticationException e) {
            throw new UnauthorizedAccessException("Invalid username or password supplied!");
        }
    }

    public TokenResponse refreshToken(String refreshToken) {

        DecodedJWT decodedToken = jwtTokenProvider.decodedToken(refreshToken);
        String username = decodedToken.getSubject();

        logger.info("Trying to get refresh token for user " + username);

        var user = usersRepository.findByEmail(username);
        if (user == null) {
            throw new UnauthorizedAccessException("Username " + username + " not found!");
        }

        return tokenProvider.refreshToken(refreshToken);
    }
}
