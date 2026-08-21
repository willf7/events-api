package com.events.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.events.api.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class TokenConfig {
    @Value("${authentication.access-secret}")
    private String secret;

    public String generateAccessToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Instant now = Instant.now();

        return JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("roles", user.getRoles().stream().map(Enum::name).toList())
                .withIssuedAt(now)
                .withExpiresAt(now.plusSeconds(1200))
                .sign(algorithm);
    }

    public Optional<JWTUserData> validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            DecodedJWT decoded = JWT.require(algorithm)
                    .build()
                    .verify(token);

            return Optional.of(
                    JWTUserData.builder()
                            .userId(UUID.fromString(decoded.getSubject()))
                            .roles(decoded.getClaim("roles").asList(String.class))
                            .build()
            );
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }
}
