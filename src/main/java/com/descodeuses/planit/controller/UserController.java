package com.descodeuses.planit.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.descodeuses.planit.dto.ChangeEmailRequest;
import com.descodeuses.planit.dto.ChangePasswordRequest;
import com.descodeuses.planit.dto.UtilisateurDto;
import com.descodeuses.planit.service.UserService;

@RestController
@RequestMapping("/api/utilisateur-list")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UtilisateurDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(UtilisateurDto::new)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/promote")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> promote(@PathVariable Long id) {
        userService.promoteToAdmin(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/demote")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> demote(@PathVariable Long id) {
        userService.demoteToUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest req, Authentication auth) {
        try {
            userService.changePassword(req.getCurrentPassword(), req.getNewPassword(), auth.getName());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/me/email")
    public ResponseEntity<?> changeEmail(@RequestBody ChangeEmailRequest req, Authentication auth) {
        try {
            userService.changeEmail(req.getNewEmail(), auth.getName());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/whoami")
    public Object whoAmI(Authentication authentication) {
        return authentication;
    }
}
