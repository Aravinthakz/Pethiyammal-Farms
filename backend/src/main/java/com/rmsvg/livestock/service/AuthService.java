package com.rmsvg.livestock.service;

import com.rmsvg.livestock.dto.AuthResponse;
import com.rmsvg.livestock.dto.LoginRequest;
import com.rmsvg.livestock.exception.ApiException;
import com.rmsvg.livestock.repository.UserAccountRepository;
import com.rmsvg.livestock.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserAccountRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(UserAccountRepository users, PasswordEncoder encoder, JwtService jwtService) {
        this.users = users;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    public AuthResponse login(LoginRequest request) {
        var user = users.findByUsername(request.username())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        String token = jwtService.generate(user.getUsername(), user.getRole().name());
        return new AuthResponse(token, user.getName(), user.getRole().name(), user.getUsername());
    }
}
