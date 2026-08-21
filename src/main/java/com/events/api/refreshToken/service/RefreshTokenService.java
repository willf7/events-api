package com.events.api.refreshToken.service;

import com.events.api.refreshToken.model.RefreshToken;
import com.events.api.refreshToken.dto.RefreshTokenRequestDTO;
import com.events.api.refreshToken.dto.RefreshTokenResponseDTO;
import com.events.api.auth.dto.LogoutRequestDTO;
import com.events.api.user.model.User;
import com.events.api.exceptions.InternalServerException;
import com.events.api.exceptions.InvalidRefreshTokenException;
import com.events.api.refreshToken.repository.RefreshTokenRepository;
import com.events.api.security.TokenConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.HexFormat;

@Service
@Slf4j
public class RefreshTokenService {
    private final RefreshTokenRepository repository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final TokenConfig tokenConfig;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, TokenConfig tokenConfig) {
        this.repository = refreshTokenRepository;
        this.tokenConfig = tokenConfig;
    }

    @Transactional
    public RefreshTokenResponseDTO refresh(RefreshTokenRequestDTO data) {
        String tokenHash = this.hashRefreshToken(data.refreshToken());
        RefreshToken refreshToken = repository.findByTokenHashAndRevokedAtIsNull(tokenHash)
                .orElseThrow(InvalidRefreshTokenException::new);
        OffsetDateTime now = OffsetDateTime.now();

        if (now.isAfter(refreshToken.getExpiresAt())) {
            refreshToken.setRevokedAt(now);
            repository.save(refreshToken);

            throw new InvalidRefreshTokenException();
        }

        String newAccessToken = tokenConfig.generateAccessToken(refreshToken.getUser());

        return new RefreshTokenResponseDTO(newAccessToken);
    }

    @Transactional
    public String createRefreshToken(User user) {
        RefreshToken newRefreshToken = new RefreshToken();
        String token = this.generateRefreshToken();

        newRefreshToken.setUser(user);
        newRefreshToken.setExpiresAt(OffsetDateTime.now().plusDays(7));
        newRefreshToken.setRevokedAt(null);
        newRefreshToken.setTokenHash(this.hashRefreshToken(token));

        repository.save(newRefreshToken);

        return token;
    }

    @Transactional
    public void revokeRefreshToken(LogoutRequestDTO data) {
        String tokenHash = this.hashRefreshToken(data.refreshToken());
        RefreshToken refreshToken = repository.findByTokenHashAndRevokedAtIsNull(tokenHash)
                .orElseThrow(InvalidRefreshTokenException::new);

        refreshToken.setRevokedAt(OffsetDateTime.now());
        repository.save(refreshToken);
    }

    private String generateRefreshToken() {
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private String hashRefreshToken(String refreshToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(refreshToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException exception) {
            log.error("Error on hashing refreshToken.", exception);

            throw new InternalServerException();
        }
    }
}
