package com.events.api.controller;

import com.events.api.domain.refreshToken.RefreshTokenRequestDTO;
import com.events.api.domain.refreshToken.RefreshTokenResponseDTO;
import com.events.api.domain.user.*;
import com.events.api.service.AuthService;
import com.events.api.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDTO> login(@Valid @RequestBody RefreshTokenRequestDTO request) {
        return ResponseEntity.ok(refreshTokenService.refresh(request));
    }

    @PostMapping("/logout")
    public void logout(@Valid @RequestBody LogoutRequestDTO request) {
        authService.logout(request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDTO> register(@Valid @RequestBody RegisterUserRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createUser(request));
    }
}
