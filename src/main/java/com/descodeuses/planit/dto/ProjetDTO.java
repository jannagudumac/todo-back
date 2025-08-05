package com.descodeuses.planit.dto;

public class ProjetDTO {
    private Long id;
    private String title;

    // Empty constructor 
    public ProjetDTO() {}
    
    // Constructor
    public ProjetDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
