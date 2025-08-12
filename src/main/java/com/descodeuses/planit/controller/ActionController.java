package com.descodeuses.planit.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.descodeuses.planit.dto.ActionDTO;
import com.descodeuses.planit.service.ActionService;

@RestController
@RequestMapping("/api/action")
public class ActionController {

    private final ActionService service;

    public ActionController(ActionService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActionDTO> getById(@PathVariable Long id) {
        ActionDTO dto = service.getActionById(id);
        return new ResponseEntity<ActionDTO>(dto, HttpStatus.OK);
    }

    @GetMapping
    // public List<ActionDTO> getAll() {
    public ResponseEntity<List<ActionDTO>> getAll() {

        // ResponseEntity
        List<ActionDTO> items = service.getAll();
        return new ResponseEntity<>(items, HttpStatus.OK);
        // return items;
    }

    @PostMapping
    public ResponseEntity<ActionDTO> create(@RequestBody ActionDTO requestDTO) {
        ActionDTO createdDTO = service.create(requestDTO);
        return new ResponseEntity<>(createdDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionDTO> update(@PathVariable Long id, @RequestBody ActionDTO requestDTO) {
        ActionDTO updatedDTO = service.update(id, requestDTO);
        return new ResponseEntity<>(updatedDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
