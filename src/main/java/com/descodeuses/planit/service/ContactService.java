package com.descodeuses.planit.service;

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
}
