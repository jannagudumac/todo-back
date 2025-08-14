package com.descodeuses.planit.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.descodeuses.planit.dto.UtilisateurDto;
import com.descodeuses.planit.service.UserService;

@RestController
@RequestMapping("/api/utilisateur-list")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UtilisateurDto> getAllUsers() {
        return userService.getAllUsers()
                          .stream()
                          .map(UtilisateurDto::new)
                          .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.notFound().build(); // 404 if user not found
        }
    }

    @GetMapping("/whoami")
    public Object whoAmI(Authentication authentication) {
    return authentication;
}


    
}
