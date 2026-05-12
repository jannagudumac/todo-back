package com.descodeuses.planit.dto;

import java.time.LocalDate;

public class ProjetDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate deadline;

    public ProjetDTO() {}

    public ProjetDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
}
