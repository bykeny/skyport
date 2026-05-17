package com.airport.auth.service;

import com.airport.auth.dto.AuthResponse;
import com.airport.auth.dto.LoginRequest;
import com.airport.auth.dto.RegisterRequest;
import com.airport.auth.exception.UnauthorizedException;
import com.airport.auth.model.Role;
import com.airport.auth.model.User;
import com.airport.auth.repository.UserRepository;
import com.airport.auth.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalStateException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() == null ? Role.PASSENGER : request.getRole());

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved);
        return new AuthResponse(saved.getId(), saved.getUsername(), saved.getRole(), token);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(user.getId(), user.getUsername(), user.getRole(), token);
    }
}
