package com.descodeuses.planit.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.descodeuses.planit.dto.ContactDTO;
import com.descodeuses.planit.service.ContactService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/contact")
public class ContactController {
    private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable Long id) {
        ContactDTO dto = service.getById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ContactDTO>> getContacts() {
        List<ContactDTO> dtos = service.getAll();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ContactDTO> createContact(@RequestBody ContactDTO contactDTO) {
        ContactDTO created = service.create(contactDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContact(@PathVariable Long id, @RequestBody ContactDTO contactDTO) {
        ContactDTO updated = service.update(id, contactDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


/* 

@RestController
@RequestMapping("/api/contact")
public class ContactController {
    private final ContactService service;
    public ContactController(ContactService service) {
        this.service = service;
    }

    @GetMapping("/hello")
    public String Hello() {
        ArrayList<Integer> liste = new ArrayList<Integer>();
        liste.add(1);
        liste.add(2);
        liste.add(3);

        return "Hello !";
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getContactById() {
        return new ResponseEntity<>("getContactById", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ContactDTO>> getContacts() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public String createContact() {        
        return "create";
    }

    @PatchMapping
    public String updateContact() {
        return "update";
    }

    @DeleteMapping
    public String deleteContact() {
        return "delete";
    }
    
} 
*/