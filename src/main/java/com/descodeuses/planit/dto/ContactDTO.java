package com.descodeuses.planit.dto;

import java.util.HashSet;
import java.util.Set;

import com.descodeuses.planit.entity.ActionEntity;


public class ContactDTO {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Set<ActionEntity> todos = new HashSet<>();

    public Set<ActionEntity> getTodos() {
        return todos;
    }

    public void setTodos(Set<ActionEntity> todos) {
        this.todos = todos;
    }
}
