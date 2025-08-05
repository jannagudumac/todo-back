package com.descodeuses.planit.dto;

public class AuthResponse {

    private String token;

    public String getToken() {
        return token;
    }

    public AuthResponse(String token) {
        this.token = token;
    }


}
