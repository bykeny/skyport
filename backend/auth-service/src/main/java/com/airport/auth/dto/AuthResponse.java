package com.airport.auth.dto;

import com.airport.auth.model.Role;

public class AuthResponse {

    private Long userId;
    private String username;
    private Role role;
    private String token;

    public AuthResponse(Long userId, String username, Role role, String token) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}
