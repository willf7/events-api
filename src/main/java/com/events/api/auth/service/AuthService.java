package com.events.api.auth.service;

import com.events.api.auth.dto.LoginRequestDTO;
import com.events.api.auth.dto.LoginResponseDTO;
import com.events.api.auth.dto.RegisterUserRequestDTO;
import com.events.api.auth.dto.RegisterUserResponseDTO;
import com.events.api.refreshToken.service.RefreshTokenService;
import com.events.api.user.model.Role;
import com.events.api.user.model.User;
import com.events.api.exceptions.DuplicateResourceException;
import com.events.api.user.repository.UserRepository;
import com.events.api.security.TokenConfig;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AuthService implements UserDetailsService {
    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository repository, AuthenticationManager authenticationManager, TokenConfig tokenConfig, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    public LoginResponseDTO login(LoginRequestDTO data) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication authentication = authenticationManager.authenticate(userAndPass);
        User user = (User) authentication.getPrincipal();
        String accessToken = tokenConfig.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    @Transactional
    public RegisterUserResponseDTO createUser(RegisterUserRequestDTO data) {
        if (repository.existsByEmail(data.email())) {
            throw new DuplicateResourceException("E-mail already registered");
        }

        User newUser = new User();
        newUser.setName(data.name());
        newUser.setEmail(data.email());
        newUser.setPassword(passwordEncoder.encode(data.password()));
        newUser.setRoles(Set.of(Role.ROLE_USER));

        repository.save(newUser);

        return new RegisterUserResponseDTO(newUser.getName(), newUser.getEmail());
    }
}
