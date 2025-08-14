package com.descodeuses.planit.dto;

import com.descodeuses.planit.entity.UtilisateurEntity;

public class UtilisateurDto {
    private Long id;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String username;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String role;
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UtilisateurDto(UtilisateurEntity entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.role = entity.getRole();
    }
}


