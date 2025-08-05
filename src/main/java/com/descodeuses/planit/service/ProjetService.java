package com.descodeuses.planit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.descodeuses.planit.dto.ProjetDTO;
import com.descodeuses.planit.entity.ProjetEntity;
import com.descodeuses.planit.repository.ProjetRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProjetService {
    private final ProjetRepository projetRepository;

    public ProjetService(ProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
    }

    // Convert Entity to DTO
    private ProjetDTO convertToDTO(ProjetEntity projet) {
        return new ProjetDTO(projet.getId(), projet.getTitle());
    }

    // Convert DTO to Entity
    private ProjetEntity convertToEntity(ProjetDTO dto) {
        ProjetEntity projet = new ProjetEntity();
        if (dto.getId() != null) {
            projet.setId(dto.getId());
        }
        projet.setTitle(dto.getTitle());
        return projet;
    }

    // GET all
    public List<ProjetDTO> getAll() {
        List<ProjetEntity> entities = projetRepository.findAll();
        List<ProjetDTO> dtos = new ArrayList<>();
        for (ProjetEntity projet : entities) {
            dtos.add(convertToDTO(projet));
        }
        return dtos;
    }

    // GET by id
    public ProjetDTO getById(Long id) {
        ProjetEntity projet = projetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projet not found with id: " + id));
        return convertToDTO(projet);
    }

    // CREATE
    public ProjetDTO create(ProjetDTO dto) {
        ProjetEntity entity = convertToEntity(dto);
        ProjetEntity saved = projetRepository.save(entity);
        return convertToDTO(saved);
    }

    // UPDATE
    public ProjetDTO update(Long id, ProjetDTO dto) {
        ProjetEntity projet = projetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projet not found with id: " + id));
        projet.setTitle(dto.getTitle());
        ProjetEntity updated = projetRepository.save(projet);
        return convertToDTO(updated);
    }

    // DELETE
    public void delete(Long id) {
        if (!projetRepository.existsById(id)) {
            throw new EntityNotFoundException("Projet not found with id: " + id);
        }
        projetRepository.deleteById(id);
    }
}
