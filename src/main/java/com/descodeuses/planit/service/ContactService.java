package com.descodeuses.planit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.descodeuses.planit.dto.ContactDTO;
import com.descodeuses.planit.entity.ContactEntity;
import com.descodeuses.planit.repository.ContactRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<ContactDTO> getAll() {
        List<ContactEntity> entities = contactRepository.findAll();
        List<ContactDTO> dtos = new ArrayList<>();

        for (ContactEntity item : entities) {
            ContactDTO dto = new ContactDTO();
            dto.setId(item.getId());
            dto.setName(item.getName());
            dtos.add(dto);
        }
        return dtos;
    }

    public ContactDTO getById(Long id) {
        ContactEntity entity = contactRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Contact not found with id: " + id));
        ContactDTO dto = new ContactDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public ContactDTO create(ContactDTO dto) {
        ContactEntity entity = new ContactEntity();
        entity.setName(dto.getName());
        ContactEntity saved = contactRepository.save(entity);
        dto.setId(saved.getId());
        return dto;
    }

    public ContactDTO update(Long id, ContactDTO dto) {
        ContactEntity entity = contactRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Contact not found with id: " + id));
        entity.setName(dto.getName());
        ContactEntity updated = contactRepository.save(entity);
        dto.setId(updated.getId());
        return dto;
    }

    public void delete(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new EntityNotFoundException("Contact not found with id: " + id);
        }
        contactRepository.deleteById(id);
    }
}




/*package com.descodeuses.planit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.descodeuses.planit.dto.ContactDTO;

import com.descodeuses.planit.entity.ContactEntity;

import com.descodeuses.planit.repository.ContactRepository;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<ContactDTO> getAll() {
        List<ContactEntity> entities = contactRepository.findAll();
        List<ContactDTO> dtos = new ArrayList<>();

        //Faire boucle sur la liste action
        for(ContactEntity item : entities) {
            ContactDTO dto = new ContactDTO();
            dto.setId(item.getId());
            dto.setName(item.getName());
            dtos.add(dto);
        }
        return dtos;
    }
}*/
