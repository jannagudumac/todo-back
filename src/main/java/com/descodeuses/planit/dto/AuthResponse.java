package com.descodeuses.planit.dto;

public class AuthResponse {

    private String token;
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public AuthResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }


}
