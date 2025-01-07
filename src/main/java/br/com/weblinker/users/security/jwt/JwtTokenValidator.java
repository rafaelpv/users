package br.com.weblinker.users.security.jwt;

import br.com.weblinker.users.validations.ValidRefreshToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Date;

public class JwtTokenValidator implements ConstraintValidator<ValidRefreshToken, String> {

    @Override
    public boolean isValid(String refreshToken, ConstraintValidatorContext context) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return false;
        }

        try {
            DecodedJWT decodedJWT = JWT.decode(refreshToken);
            return decodedJWT.getExpiresAt().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
