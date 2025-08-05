package com.descodeuses.planit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.descodeuses.planit.dto.ProjetDTO;
import com.descodeuses.planit.service.ProjetService;

@RestController
@RequestMapping("/api/projet")
public class ProjetController {

    private final ProjetService projetService;

    public ProjetController(ProjetService projetService) {
        this.projetService = projetService;
    }

    @GetMapping
    public ResponseEntity<List<ProjetDTO>> getAll() {
        List<ProjetDTO> projets = projetService.getAll();
        return new ResponseEntity<>(projets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetDTO> getById(@PathVariable Long id) {
        ProjetDTO projet = projetService.getById(id);
        return new ResponseEntity<>(projet, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProjetDTO> create(@RequestBody ProjetDTO requestDTO) {
        ProjetDTO created = projetService.create(requestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetDTO> update(@PathVariable Long id, @RequestBody ProjetDTO dto) {
        ProjetDTO updated = projetService.update(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projetService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
