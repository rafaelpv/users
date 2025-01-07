package br.com.weblinker.users.security.jwt;

import br.com.weblinker.users.dto.TokenResponse;
import br.com.weblinker.users.exceptions.UnauthorizedAccessException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key:default}")
    private String secretKey = "default";

    @Value("${security.jwt.token.expire-length:default}")
    private Long validityInMilliseconds = 3_600_000L;

    @Autowired
    private UserDetailsService userDetailsService;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenResponse createAccessToken(String username, List<String> roles, Long companyId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        String accessToken = getAccessToken(username, roles, companyId, now, validity);
        String refreshToken = getRefreshToken(username, roles, companyId, now);

        return new TokenResponse(
                username,
                true,
                now,
                validity,
                accessToken,
                refreshToken
        );
    }

    public TokenResponse refreshToken(String refreshToken) {
        String token = "";
        if (refreshToken.contains("Bearer ")) {
            token = refreshToken.substring("Bearer ".length());
        }

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        Long companyId = decodedJWT.getClaim("companyId").asLong();

        return createAccessToken(username, roles, companyId);
    }


    public String getAccessToken(String username, List<String> roles, Long companyId, Date now, Date validity) {
        String issuerURL = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create()
                .withClaim("roles", roles)
                .withClaim("companyId", companyId)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withSubject(username)
                .withIssuer(issuerURL)
                .sign(algorithm)
                .trim();
    }

    public String getRefreshToken(String username, List<String> roles, Long companyId, Date now) {
        Date validity = new Date(now.getTime() + validityInMilliseconds * 3);
        return JWT.create()
                .withClaim("roles", roles)
                .withClaim("companyId", companyId)
                .withExpiresAt(validity)
                .withSubject(username)
                .sign(algorithm)
                .trim();
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT jwt = decodedToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwt.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public DecodedJWT decodedToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        return jwtVerifier.verify(token);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Boolean validateToken(String token) {
        DecodedJWT jwt = decodedToken(token);
        try {
            return !jwt.getExpiresAt().before(new Date());
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Expired or invalid JWT token!");
        }
    }
}
