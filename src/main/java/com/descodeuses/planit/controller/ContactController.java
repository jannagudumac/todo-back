package com.descodeuses.planit.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.descodeuses.planit.dto.ContactDTO;
import com.descodeuses.planit.service.ContactService;

import org.springframework.web.bind.annotation.PostMapping;




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
